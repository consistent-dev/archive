package conf;

import util.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * 3초마다 한 번씩 설정 파일을 읽어서 property를 갱신한다.
 *
 * -Dhome.path=/Users/{yourName}/consistent-dev
 * -Dconf.path=./conf
 * -Dconf.name=my-server.conf
 */
public abstract class ServerConfig extends Thread{

    private String fileName = "server.conf";
    private boolean running = true;
    private long lastChecked = DateUtil.now();
    private String confPath = ".";
    private long lastLoaded = -1;
    public Properties property = new Properties();
    protected ServerConfig(String fileName){
        this.fileName = fileName;
    }

    @Override
    public void run() {
        while(running){
            reload(false);
            ThreadUtil.sleep(3000);
        }
    }

    public synchronized boolean reload(boolean force){
        // 3초마다 한 번씩 reload
        long now = DateUtil.now();
        if(force == false && now < this.lastChecked + 3000){
            return false;
        }
        this.lastChecked = now;

        // conf 파일을 읽는다.
        File file = getPropertyFile();

        // 수정되지 않았으면 reload하지 않는다.
        if(file.lastModified() == this.lastLoaded){
            return false;
        }
        this.lastLoaded = file.lastModified();

        // 파일 읽어서 properties에 저장
        Properties temp = new Properties();
        if(file.canRead()){
            FileInputStream in = null;
            try {
                in = new FileInputStream(file);
                temp.load(in);
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                FileUtil.close(in);
            }
        }
        this.property = ConfigValueUtil.replaceSysProp(temp);
        apply();
        return true;
    }

    public File getPropertyFile(){
        this.confPath = System.getProperty("conf.path", ".");
        if(this.confPath.startsWith(".")){
            String home = System.getProperty("home.path", ".");
            this.confPath = new File(home, this.confPath).getAbsolutePath();
            System.setProperty("conf.path", this.confPath);
        }
        String confFileName = System.getProperty("conf.name", this.fileName);
        return new File(this.confPath, confFileName.trim());
    }

    protected abstract void apply();

    public boolean getBoolean(String key, boolean def){
        try{
            String v = getValue(key);
            if(v != null){
                return Boolean.parseBoolean(v);
            }
        }catch (Throwable e){
            ExceptionUtil.ignore(e);
        }
        return def;
    }

    public String getValue(String key){
        return StringUtil.trim(property.getProperty(key));
    }

    public String getValue(String key, String def){
        return StringUtil.trim(property.getProperty(key, def));
    }
}
