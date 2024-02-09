package com.tsdb.tsfile.memory;


import com.tsdb.common.data.DataType;

/**
 * Time series data memory model, for read
 */
public class ReadTVList extends TVList {

    public ReadTVList(DataType[] columnDataType) {
        super(columnDataType);
    }
}
