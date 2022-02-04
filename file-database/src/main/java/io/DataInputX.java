package io;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;

public class DataInputX {

    private int offset;
    private DataInput inner;

    public DataInputX(byte[] buff){
        this(new ByteArrayInputStream(buff));
    }

    private DataInputX(ByteArrayInputStream in){
        this(new DataInputStream(in));
    }

    private DataInputX(DataInputStream in){
        this.inner = in;
    }
}
