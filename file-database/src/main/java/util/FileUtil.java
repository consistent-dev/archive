package util;

import java.io.File;

public class FileUtil {

    private FileUtil(){}

    private static final String basePath = System.getProperty("user.dir");

    public static File getFile(String fileName){
        return new File(basePath, fileName);
    }

}
