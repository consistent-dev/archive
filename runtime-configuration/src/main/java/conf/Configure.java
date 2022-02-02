package conf;

import util.ThreadUtil;

public class Configure extends ServerConfig {
    private static Configure instance = null;

    public boolean debug_test = false;
    public String user_name = "";

    public final static synchronized Configure getInstance(){
        if(instance == null){
            instance = new Configure();
            instance.setDaemon(true);
            instance.setName(ThreadUtil.getName(instance));
            instance.reload(true);
            instance.start();
        }
        return instance;
    }

    private Configure(){
        super("my-server.conf");
    }

    @Override
    protected void apply() {
        this.debug_test = getBoolean("debug_test", false);
        this.user_name = getValue("user_name", "default_user_name");
    }
}
