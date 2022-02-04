package tutorial;

import util.ExceptionUtil;
import util.FileUtil;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileReaderTest {

    public static void main(String[] args) {
        File file = FileUtil.getFile("test1.txt");
        try {
            if(file.exists() == false){
                file.createNewFile();
            }

            try(FileReader fileReader = new FileReader(file);
                FileWriter fileWriter = new FileWriter(file);){

                fileWriter.write('1');
                fileWriter.write('R');
                fileWriter.flush();

                System.out.println((char)fileReader.read());
                System.out.println((char)fileReader.read());
            } catch (IOException e) {
                ExceptionUtil.ignore(e);
            }
        } catch (IOException e) {
            ExceptionUtil.ignore(e);
        }


    }
}
