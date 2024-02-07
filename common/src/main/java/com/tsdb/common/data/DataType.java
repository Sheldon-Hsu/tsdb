package com.tsdb.common.data;

public enum  DataType {
    INTEGER(0),
    LONG(1),
    DOUBLE(2),
    FLOAT(3),
    BOOLEAN(3),
    ARRAY(3),
    VARCHAR(4);

    private int code;
    DataType(int code){
        this.code = code;
    }
}
