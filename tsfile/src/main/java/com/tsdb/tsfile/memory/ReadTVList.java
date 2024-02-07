package com.tsdb.tsfile.memory;


import com.tsdb.common.data.DataType;

import java.util.List;

/**
 * Time series data memory model, for read
 */
public class ReadTVList extends TVList {

    public ReadTVList(long[] timestamps, List<List<Object>> values, DataType dataTypes) {
        super(timestamps, values, dataTypes);
    }
}
