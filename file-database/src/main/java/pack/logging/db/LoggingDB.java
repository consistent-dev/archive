package pack.logging.db;

import java.io.File;
import java.util.Objects;

public class LoggingDB {

    private static LoggingDB instance;
    private final File root;
    private LoggingDB(String subPath){
        this.root = new File(System.getProperty("user.dir"), subPath);
    }

    public static synchronized LoggingDB getInstance(){
        if(instance == null){
            instance = new LoggingDB("logging.db");
        }
        return instance;
    }

    public File getRoot() {
        return root;
    }
}
