package com.tsdb.tsfile.exception.write;

import com.tsdb.tsfile.exception.TsFileRuntimeException;

public class UnsupportedColumnTypeException extends TsFileRuntimeException {

    private static final long serialVersionUID = -1853067734609996375L;

    public UnsupportedColumnTypeException(String type) {
        super("Column type not supported: " + type);
    }
}
