package tutorial;

import util.ExceptionUtil;
import util.FileUtil;
import util.StopWatch;

import java.io.*;

public class BufferedFileInputStreamTest {

    public static void main(String[] args) {
        File file = FileUtil.getFile("test1.txt");
        File copy = FileUtil.getFile("copy.txt");
        try {
            if(file.exists() == false){
                file.createNewFile();
            }
            if(copy.exists() == false){
                copy.createNewFile();
            }

            try(FileInputStream fis = new FileInputStream(file);
                FileInputStream cfis = new FileInputStream(copy);
                // Buffered 스트림은 8192바이트 크기의 배열을 가지고 있습니다. 8K 정보를 한 번에 읽고 쓸 수 있습니다.
                BufferedInputStream bfis = new BufferedInputStream(new FileInputStream(file));
                FileOutputStream fos = new FileOutputStream(file, true);
                FileOutputStream cfos = new FileOutputStream(copy);
                BufferedOutputStream bcfos = new BufferedOutputStream(new FileOutputStream(copy));){

                // write sample data
                for(int j=0; j<1_000_000; j++){
                    fos.write(j);
                }
                fos.flush();

                int i = 0;
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                while((i = fis.read()) != -1){
                    cfos.write(i);
                }
                System.out.println(stopWatch.stop());

                i = 0;
                stopWatch.start();
                while((i = bfis.read()) != -1){
                    bcfos.write(i);
                }
                System.out.println(stopWatch.stop());
            } catch (IOException e) {
                ExceptionUtil.ignore(e);
            }
        } catch (IOException e) {
            ExceptionUtil.ignore(e);
        }


    }
}
