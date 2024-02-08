package com.tsdb.common.data;

public enum  DataType {
    INTEGER((byte)0),
    LONG((byte)1),
    DOUBLE((byte)2),
    FLOAT((byte)3),
    BOOLEAN((byte)3),
    ARRAY((byte)3),
    VARCHAR((byte)4);

    private final byte code;
    DataType(byte code){
        this.code = code;
    }


    public byte getCode(){
        return code;
    }
}
