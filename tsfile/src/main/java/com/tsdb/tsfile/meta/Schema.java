package com.tsdb.tsfile.meta;

import com.tsdb.common.data.DataType;
import com.tsdb.tsfile.encoding.encode.Encoder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Schema implements Serializable {
    private static final long serialVersionUID = 2103464296109291362L;

    private Map<DataType, Encoder> encoder = new HashMap<>();


    public void setEncoder(Map<DataType, Encoder> encoder) {
        this.encoder = encoder;
    }

    public Map<DataType, Encoder> getEncoders() {
        return encoder;
    }
}
