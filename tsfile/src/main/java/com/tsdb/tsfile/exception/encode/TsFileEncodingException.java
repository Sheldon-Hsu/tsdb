package com.tsdb.tsfile.exception.encode;

import com.tsdb.tsfile.exception.TsFileRuntimeException;

public class TsFileEncodingException extends TsFileRuntimeException {

    private static final long serialVersionUID = -3786054735464888523L;

    public TsFileEncodingException(String message) {
        super(message);
    }
}
