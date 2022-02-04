package tutorial;

import util.ExceptionUtil;
import util.FileUtil;

import java.io.*;

public class FileInputStreamTest {

    public static void main(String[] args) {
        File file = FileUtil.getFile("test1.txt");
        try {
            if(file.exists() == false){
                file.createNewFile();
            }

            try(FileInputStream fis = new FileInputStream(file);
                FileOutputStream fos = new FileOutputStream(file,true);){

                fos.write(1);
                fos.write('R');
                // 출력 스트림의 close() 메서드 안에서 flush() 메서드를 호출합니다.
                // 저장만하고 다시 읽지 않는다면 flush()를 호출하지 않아도 됩니다.
                fos.flush();

                System.out.println(fis.read());
                System.out.println((char)fis.read());
            } catch (IOException e) {
                ExceptionUtil.ignore(e);
            }
        } catch (IOException e) {
            ExceptionUtil.ignore(e);
        }


    }
}
