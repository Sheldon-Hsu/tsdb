package com.tsdb.tsfile.exception.compress;

public class GZIPCompressOverflowException extends RuntimeException {

    private static final long serialVersionUID = 8439854241857166034L;

    public GZIPCompressOverflowException() {
        super("compressed data is larger than the given byte container.");
    }
}
