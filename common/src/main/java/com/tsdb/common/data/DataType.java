package com.tsdb.common.data;

public enum DataType {
    BOOLEAN((byte) 0),
    INTEGER((byte) 1),
    FLOAT((byte) 2),
    LONG((byte) 3),
    DOUBLE((byte) 4),
    VARCHAR((byte) 5),
    ARRAY((byte) 6),
    BINARY((byte) 7);

    private final byte code;

    DataType(byte code) {
        this.code = code;
    }

    public int getFixLength() {
        switch (this) {
            case BOOLEAN:
                return 1;
            case INTEGER:
            case FLOAT:
                return 4;
            case LONG:
            case DOUBLE:
                return 8;
            case VARCHAR:
            default:
                return 0;
        }
    }

    public byte getCode() {
        return code;
    }
}
