package tutorial;

import util.ExceptionUtil;
import util.FileUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileReaderTest {

    public static void main(String[] args) {
        File file = FileUtil.getFile("test1.txt");
        try {
            if(file.exists() == false){
                file.createNewFile();
            }

            try(java.io.FileReader fileReader = new java.io.FileReader(file);
                FileWriter fileWriter = new FileWriter(file);){

                fileWriter.write('C');
                fileWriter.write('R');
                fileWriter.flush();

                System.out.println(fileReader.read());
                System.out.println(fileReader.read());
            } catch (IOException e) {
                ExceptionUtil.ignore(e);
            }
        } catch (IOException e) {
            ExceptionUtil.ignore(e);
        }


    }
}
