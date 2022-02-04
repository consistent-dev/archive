package tutorial;

import util.ExceptionUtil;
import util.FileUtil;
import util.StopWatch;

import java.io.*;

/**
 * FileInputStream은 사람이 읽고 쓰는 텍스트 형식의 데이터를 다루었습니다.
 * DataInputStream, DataOutputStream은 메모리에 저장된 0,1 상태를 그대로 읽거나 씁니다.
 * 따라서 자료형의 크기가 그대로 보존됩니다. 자료형을 그대로 읽고 쓰는 스틀미이기 떄문에 같은 정수라도 자료형에 따라 다르게 처리됩니다.
 * 자료를 쓸 때 사용한 메서드와 같은 자료형의 메서드로 읽어야 합니다.
 */
public class DataFileInputStreamTest {

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

            try(DataInputStream dis = new DataInputStream(new FileInputStream(file));
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
                ){
                dos.write(1024+8); // 하위 8비트만 write 0010 0000 1000
                dos.writeInt(1024+8); // 32비트 모두 write 0010 0000 0000
                System.out.println(dis.read());
                System.out.println(dis.readInt());
            } catch (IOException e) {
                ExceptionUtil.ignore(e);
            }
        } catch (IOException e) {
            ExceptionUtil.ignore(e);
        }


    }
}
