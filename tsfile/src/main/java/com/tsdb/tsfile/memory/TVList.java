package com.tsdb.tsfile.memory;

import com.tsdb.common.data.DataType;

import java.util.List;

/**
 *  Time series data memory model, for write.
 */
public class TVList {

    protected long [] timestamps;
    protected List<List<Object>> values;
    protected DataType dataTypes;

    public TVList(long[] timestamps, List<List<Object>> values, DataType dataTypes) {
        this.timestamps = timestamps;
        this.values = values;
        this.dataTypes = dataTypes;
    }



}
