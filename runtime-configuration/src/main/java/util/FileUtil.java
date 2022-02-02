package util;

import java.io.InputStream;

public class FileUtil {

    private FileUtil(){}

    public static InputStream close(InputStream in){
        try{
            if(in != null){
                in.close();
            }
        } catch (Throwable e) {
            ExceptionUtil.ignore(e);
        }
        return null;
    }
}
