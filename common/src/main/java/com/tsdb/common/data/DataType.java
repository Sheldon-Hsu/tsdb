package com.tsdb.common.data;

import java.sql.Types;

public enum DataType {
    BOOLEAN(Types.BOOLEAN),
    SMALLINT(Types.SMALLINT),
    INTEGER(Types.INTEGER),
    FLOAT(Types.FLOAT),
    BIGINT(Types.BIGINT),
    DOUBLE(Types.DOUBLE),
    VARCHAR(Types.VARCHAR),
    ARRAY(Types.ARRAY),
    BINARY(Types.BINARY),
    DATE(Types.DATE),
    TIMESTAMP(Types.TIMESTAMP)
    ;

    private final Integer code;

    DataType(Integer code) {
        this.code = code;
    }

    public int getFixLength() {
        switch (this) {
            case BOOLEAN:
                return 1;
            case SMALLINT:
                return 2;
            case INTEGER:
            case FLOAT:
                return 4;
            case BIGINT:
            case DOUBLE:
            case TIMESTAMP:
            case DATE:
                return 8;
            case VARCHAR:
            case ARRAY:
            case BINARY:
            default:
                return 0;
        }
    }

    public Integer getCode() {
        return code;
    }
}
