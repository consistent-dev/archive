package io;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DataInputX {

    private int offset;
    private DataInput inner; // DataInputStream, RandomAccessFile 모두를 참조하기 위해 DataInput 인터페이스 타입으로 지정

    public DataInputX(DataInputStream in){
        this.inner = in;
    }

    public DataInputX(BufferedInputStream in){
        this.inner = new DataInputStream(in);
    }
    // TODO
//    public DataInputX(ByteArrayInputStream in){
//        this(new DataInputStream(in));
//    }
    // TODO
//    public DataInputX(RandomAccessFile in){
//        this.inner = in;
//    }

    /**
     * InputStream에서 1바이트를 읽습니다.
     */
    public byte readByte(){
        this.offset += 1;
        try{
            return this.inner.readByte();
        } catch (IOException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * InputStream에서 2바이트를 읽습니다.
     */
    public short readShort(){
        this.offset += 2;
        try{
            return this.inner.readShort();
        } catch (IOException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * InputStream에서 4바이트를 읽습니다.
     */
    public int readInt(){
        this.offset += 4;
        try{
            return this.inner.readInt();
        } catch (IOException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * InputStream에서 8바이트를 읽습니다.
     */
    public long readLong(){
        this.offset += 8;
        try{
            return this.inner.readLong();
        } catch (IOException e) {
            throw new DataIOException(e);
        }
    }

    /**
     * InputStream에서 len 바이트를 읽어서 byte[]에 저장합니다.
     */
    public byte[] read(int len){
        this.offset += len;
        byte[] buff = new byte[len];
        try{
            this.inner.readFully(buff);
        } catch (IOException e) {
            throw new DataIOException(e);
        }
        return buff;
    }

    public boolean readBoolean() {
        this.offset += 1;
        try {
            return this.inner.readBoolean();
        } catch (IOException e) {
            throw new DataIOException(e);
        }
    }

    public Map<String, String> readMap(){
        long sz = this.readDecimal();
        Map<String, String> ret = new HashMap<>();
        for(int i=0; i<sz; i++){
            String key = this.readText();
            String value = this.readText();
            ret.put(key, value);
        }
        return ret;
    }

    public Map<String, String> readMapNullable(){
        boolean exist = this.readBoolean();
        return exist ? this.readMap() : null;
    }

    /**
     * byte[]에서 pos부터 3바이트만큼 int로 변환합니다.
     */
    public static int toInt3(byte[] buf, int pos){
        int ch1 = buf[pos+0] & 0xFF; // 첫 번째 바이트 숫자
        int ch2 = buf[pos+1] & 0xFF; // 두 번째 바이트 숫자
        int ch3 = buf[pos+2] & 0xFF; // 세 번째 바이트 숫자
        return ((ch1 << 16) + (ch2 << 8) + (ch3));
    }

    /**
     * byte[]에서 pos부터 5바이트만큼 long으로 변환합니다.
     */
    public static long toLong5(byte[] buf, int pos){
        long ch1 = buf[pos+0] & 0xFF;
        long ch2 = buf[pos+1] & 0xFF;
        long ch3 = buf[pos+2] & 0xFF;
        long ch4 = buf[pos+3] & 0xFF;
        long ch5 = buf[pos+4] & 0xFF;
        return (ch1 << 32) + (ch2 << 24) + (ch3 << 16) + (ch4 << 8) + (ch5);
    }

    public int readInt3(){
        byte[] buf = read(3);
        return toInt3(buf, 0);
    }

    public long readLong5(){
        byte[] buf = read(5);
        return toLong5(buf, 0);
    }

    /**
     * 숫자를 Write할 때 바이트 길이를 먼저 쓰고 값을 쓰는 규칙을 사용합니다.
     */
    public long readDecimal(){
        return readDecimal(readByte());
    }

    private long readDecimal(byte len){
        switch (len) {
            case 0:
                return 0;
            case 1:
                return readByte();
            case 2:
                return readShort();
            case 3:
                return readInt3();
            case 4:
                return readInt();
            case 5:
                return readLong5();
            case 8:
                return readLong();
            default:
                return readLong();
        }
    }

    public String readText() {
        byte[] buffer = readBlob();
        try {
            return new String(buffer, "UTF8");
        } catch (IOException e) {
            throw new DataIOException(e);
        }
    }

    private static final int EMPTY_VALUE_PREFIX = 0;
    private static final int BLOB_PREFIX = 254; // byte[]의 길이가 TINYBLOB_MAX를 넘는다는 것을 나타내기 위한 값
    private static final int MEDIUMBLOB_PREFIX = 255; // byte[]의 길이가 BLOB_MAX를 넘는다는 것을 나타내기 위한 값
    /**
     * MYSQL BLOB Maximum Length
     *
     * TINYBLOB  : maximum length of 255 bytes
     * BLOB      : maximum length of 65,535 bytes
     * MEDIUMBLOB: maximum length of 16,777,215 bytes
     * LONGBLOB  : maximum length of 4,294,967,295 bytes
     *
     * unSignedByte  = 1바이트 = 8비트,  2^8 = 256
     * unSignedShort = 2바이트 = 16비트, 2^16 = 65536
     *
     * value 길이에 따른 데이터 프로토콜
     * 1. 253이하   : [value 길이(1~253) 1바이트][value]
     * 2. 65535이하 : [254 1바이트][value 길이 2바이트][value]
     * 3. 65535초과 : [255 1바이트][value 길이 4바이트][value]
     */
    public byte[] readBlob() {
        int baselen = readUnsignedByte();
        switch (baselen) {
            case BLOB_PREFIX: {
                int len = readUnsignedShort();
                byte[] buffer = read(len);
                return buffer;
            }
            case MEDIUMBLOB_PREFIX: {
                int len = this.readInt();
                byte[] buffer = read(len);
                return buffer;
            }
            case EMPTY_VALUE_PREFIX: {
                return new byte[0];
            }
            default:
                byte[] buffer = read(baselen);
                return buffer;
        }
    }

    public int readUnsignedByte() {
        this.offset += 1;
        try {
            return this.inner.readUnsignedByte();
        } catch (IOException e) {
            throw new DataIOException(e);
        }
    }

    public int readUnsignedShort() {
        this.offset += 2;
        try {
            return this.inner.readUnsignedShort();
        } catch (IOException e) {
            throw new DataIOException(e);
        }
    }
}
