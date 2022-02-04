package io;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class DataOutputX {

    public final static int INT3_MIN_VALUE = 0xff800000;
    public final static int INT3_MAX_VALUE = 0x007fffff;

    public final static long LONG5_MIN_VALUE = 0xffffff8000000000L;
    public final static long LONG5_MAX_VALUE = 0x0000007fffffffffL;

    private int written;
    private DataOutput outter; // DataOutputStream, RandomAccessFile 모두를 참조하기 위해 DataInput 인터페이스 타입으로 지정

    public DataOutputX(DataOutputStream out){
        this.outter = out;
    }

    public DataOutputX(BufferedOutputStream out){
        this.outter = new DataOutputStream(out);
    }
    // TODO
//    public DataInputX(ByteArrayInputStream in){
//        this(new DataInputStream(in));
//    }
    // TODO
//    public DataInputX(RandomAccessFile in){
//        this.inner = in;
//    }

    public DataOutputX writeDecimal(long v) {
        if (v == 0) {
            writeByte(0);
        } else if (Byte.MIN_VALUE <= v && v <= Byte.MAX_VALUE) {
            byte[] b = new byte[2];
            b[0] = 1;
            b[1] = (byte) v;
            write(b);
        } else if (Short.MIN_VALUE <= v && v <= Short.MAX_VALUE) {
            byte[] b = new byte[3];
            b[0] = 2;
            toBytes(b, 1, (short) v);
            write(b);
        } else if (INT3_MIN_VALUE <= v && v <= INT3_MAX_VALUE) {
            byte[] b = new byte[4];
            b[0] = 3;
            write(toBytes3(b, 1, (int) v), 0, 4);
        } else if (Integer.MIN_VALUE <= v && v <= Integer.MAX_VALUE) {
            byte[] b = new byte[5];
            b[0] = 4;
            write(toBytes(b, 1, (int) v), 0, 5);
        } else if (LONG5_MIN_VALUE <= v && v <= LONG5_MAX_VALUE) {
            byte[] b = new byte[6];
            b[0] = 5;
            write(toBytes5(b, 1, v), 0, 6);
        } else if (Long.MIN_VALUE <= v && v <= Long.MAX_VALUE) {
            byte[] b = new byte[9];
            b[0] = 8;
            write(toBytes(b, 1, v), 0, 9);
        }
        return this;
    }

    /**
     * OutputStream에 1바이트를 씁니다.
     */
    public DataOutputX writeByte(int v){
        this.written += 1;
        try{
            this.outter.writeByte((byte)v);
        } catch (IOException e) {
            throw new DataIOException(e);
        }
        return this;
    }

    public DataOutputX writeBoolean(boolean v) {
        this.written++;
        try {
            this.outter.writeBoolean(v);
        } catch (IOException e) {
            throw new DataIOException(e);
        }
        return this;
    }

    public DataOutputX writeShort(int v) {
        this.written += 2;
        try {
            this.outter.write(toBytes((short) v));
        } catch (IOException e) {
            throw new DataIOException(e);
        }
        return this;
    }

    public DataOutputX writeInt(int v) {
        this.written += 4;
        try {
            this.outter.write(toBytes(v));
        } catch (IOException e) {
            throw new DataIOException(e);
        }
        return this;
    }


    public DataOutputX writeLong(long v) {
        this.written += 8;
        try {
            this.outter.write(toBytes(v));
        } catch (IOException e) {
            throw new DataIOException(e);
        }
        return this;
    }

    public DataOutputX writeFloat(float v) {
        this.written += 4;
        try {
            this.outter.write(toBytes(v));
        } catch (IOException e) {
            throw new DataIOException(e);
        }
        return this;
    }

    public DataOutputX writeDouble(double v) {
        this.written += 8;
        try {
            this.outter.write(toBytes(v));
        } catch (IOException e) {
            throw new DataIOException(e);
        }
        return this;
    }

    public DataOutputX writeText(String s) {
        if (s == null) {
            writeByte((byte) 0);
        } else {
            writeBlob(s.getBytes(StandardCharsets.UTF_8));
        }
        return this;
    }


    private static final int TINYBLOB_MAX = 253; // TINYBLOB의 최대 길이를 255로 사용하면, 첫 바이트만 읽어서 데이터의 길이인지 PREFIX인지 구분하지 못한다.
    private static final int BLOB_MAX = 65535;
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
     *
     * RandomAccessFile#writeBytes(String s), DataOutputStream#writeBytes(String s)의 로직이 달라서 하나로 통합
     *
     */
    public DataOutputX writeBlob(byte[] value) {
        if (value == null || value.length == 0) {
            writeByte((byte) EMPTY_VALUE_PREFIX);
        } else {
            int len = value.length;
            if (len <= TINYBLOB_MAX) {
                writeByte((byte) len);
                write(value);
            } else if (len <= BLOB_MAX) {
                byte[] buff = new byte[3];
                buff[0] = (byte) BLOB_PREFIX;
                write(toBytes(buff, 1, (short) len));
                write(value);
            } else {
                byte[] buff = new byte[5];
                buff[0] = (byte) MEDIUMBLOB_PREFIX;
                write(toBytes(buff, 1, len));
                write(value);
            }
        }
        return this;
    }


    public DataOutputX write(byte[] b) {
        this.written += b.length;
        try {
            this.outter.write(b);
        } catch (IOException e) {
            throw new DataIOException(e);
        }
        return this;
    }

    public DataOutputX write(byte[] b, int off, int len) {
        this.written += len;
        try {
            this.outter.write(b, off, len);
        } catch (IOException e) {
            throw new DataIOException(e);
        }
        return this;
    }

    public DataOutputX writeMap(Map<String, String> map){
        if(map == null){
            throw new DataIOException("Map can not be null");
        }
        this.writeDecimal(map.size());
        for(String key : map.keySet()){
            this.writeText(key);
            this.writeText(map.get(key));
        }
        return this;
    }

    public DataOutputX writeMapNullable(Map<String, String> map){
        if(map != null && map.size() > 0) {
            this.writeBoolean(true);
            this.writeMap(map);
        }else{
            this.writeBoolean(false);
        }
        return this;
    }


    public int size() {
        return this.written;
    }


    public static byte[] toBytes(short v){
        byte[] buf = new byte[2];
        buf[0] = (byte) ((v >>> 8) & 0xFF);
        buf[1] = (byte) ((v >>> 0) & 0xFF);
        return buf;
    }

    public static byte[] toBytes(byte[] buf, int off, short v) {
        buf[off] = (byte) ((v >>> 8) & 0xFF);
        buf[off + 1] = (byte) ((v >>> 0) & 0xFF);
        return buf;
    }

    public static byte[] toBytes(int v) {
        byte buf[] = new byte[4];
        buf[0] = (byte) ((v >>> 24) & 0xFF);
        buf[1] = (byte) ((v >>> 16) & 0xFF);
        buf[2] = (byte) ((v >>> 8) & 0xFF);
        buf[3] = (byte) ((v >>> 0) & 0xFF);
        return buf;
    }

    public static byte[] toBytes(byte[] buf, int off, int v) {
        buf[off] = (byte) ((v >>> 24) & 0xFF);
        buf[off + 1] = (byte) ((v >>> 16) & 0xFF);
        buf[off + 2] = (byte) ((v >>> 8) & 0xFF);
        buf[off + 3] = (byte) ((v >>> 0) & 0xFF);
        return buf;
    }

    public static byte[] toBytes(long v) {
        byte buf[] = new byte[8];
        buf[0] = (byte) (v >>> 56);
        buf[1] = (byte) (v >>> 48);
        buf[2] = (byte) (v >>> 40);
        buf[3] = (byte) (v >>> 32);
        buf[4] = (byte) (v >>> 24);
        buf[5] = (byte) (v >>> 16);
        buf[6] = (byte) (v >>> 8);
        buf[7] = (byte) (v >>> 0);
        return buf;
    }

    public static byte[] toBytes(byte[] buf, int off, long v) {
        buf[off] = (byte) (v >>> 56);
        buf[off + 1] = (byte) (v >>> 48);
        buf[off + 2] = (byte) (v >>> 40);
        buf[off + 3] = (byte) (v >>> 32);
        buf[off + 4] = (byte) (v >>> 24);
        buf[off + 5] = (byte) (v >>> 16);
        buf[off + 6] = (byte) (v >>> 8);
        buf[off + 7] = (byte) (v >>> 0);
        return buf;
    }

    public static byte[] toBytes3(int v) {
        byte buf[] = new byte[3];
        buf[0] = (byte) ((v >>> 16) & 0xFF);
        buf[1] = (byte) ((v >>> 8) & 0xFF);
        buf[2] = (byte) ((v >>> 0) & 0xFF);
        return buf;
    }

    public static byte[] toBytes3(byte[] buf, int off, int v) {
        buf[off] = (byte) ((v >>> 16) & 0xFF);
        buf[off + 1] = (byte) ((v >>> 8) & 0xFF);
        buf[off + 2] = (byte) ((v >>> 0) & 0xFF);
        return buf;
    }

    public static byte[] toBytes5(long v) {
        byte buf[] = new byte[5];
        buf[0] = (byte) (v >>> 32);
        buf[1] = (byte) (v >>> 24);
        buf[2] = (byte) (v >>> 16);
        buf[3] = (byte) (v >>> 8);
        buf[4] = (byte) (v >>> 0);
        return buf;
    }

    public static byte[] toBytes5(byte[] buf, int off, long v) {
        buf[off] = (byte) (v >>> 32);
        buf[off + 1] = (byte) (v >>> 24);
        buf[off + 2] = (byte) (v >>> 16);
        buf[off + 3] = (byte) (v >>> 8);
        buf[off + 4] = (byte) (v >>> 0);
        return buf;
    }

    public static byte[] toBytes(float v) {
        return toBytes(Float.floatToIntBits(v));
    }

    public static byte[] toBytes(double v) {
        return toBytes(Double.doubleToLongBits(v));
    }


    public void close() {
        try {
            if (this.outter instanceof RandomAccessFile) {
                ((RandomAccessFile) this.outter).close();
            } else
                if (this.outter instanceof OutputStream) {
                ((OutputStream) this.outter).close();
            }
        } catch (IOException e) {
            throw new DataIOException(e);
        }
    }

    public void flush() {
        if (this.outter instanceof OutputStream) {
            try {
                ((OutputStream) this.outter).flush();
            } catch (IOException e) {
                throw new DataIOException(e);
            }
        }
    }

}
