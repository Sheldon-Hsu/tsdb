package com.tsdb.tsfile.exception;


public class TsFileRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1222766463206157354L;

    public TsFileRuntimeException(String message) {
        super(message);
    }
}
