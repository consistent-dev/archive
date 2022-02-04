package root;

import io.DataInputX;
import io.DataOutputX;
import pack.LoggingPack;
import pack.logging.db.LoggingDB;
import util.DateUtil;
import util.ExceptionUtil;

import java.io.*;

public class App {

    public static void main(String[] args) {
        LoggingDB loggingDB = LoggingDB.getInstance();
        try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(loggingDB.getRoot())));
            DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(loggingDB.getRoot())));
        ){
            DataOutputX dout = new DataOutputX(dos);
            DataInputX din = new DataInputX(dis);
            for(int i=0; i< 10; i++){
                LoggingPack p = new LoggingPack(DateUtil.now(), i, "testCategory" + i, 100 + i,"testContent");
                p.addTag("testKey", "testValue");
                if(i %2 == 0){
                    p.addField("testFieldKey", "testFieldValue");
                }
                p.write(dout).flush();
            }

            for(int i=0; i< 10; i++) {
                LoggingPack readPack = (LoggingPack) new LoggingPack().read(din);
                System.out.println(readPack);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
