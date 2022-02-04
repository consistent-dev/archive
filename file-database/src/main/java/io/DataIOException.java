package io;

public class DataIOException extends RuntimeException{

    private DataIOException(){}

    public DataIOException(String message){
        super(message);
    }

    public DataIOException(Throwable t){
        super(t);
    }

    public DataIOException(String message, Throwable t){
        super(message, t);
    }
}
