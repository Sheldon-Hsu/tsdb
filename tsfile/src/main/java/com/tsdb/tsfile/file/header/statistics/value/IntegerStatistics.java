package com.tsdb.tsfile.file.header.statistics.value;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class IntegerStatistics extends ValueStatistics <Integer>{
    @Override
    public void deserialize(InputStream inputStream) throws IOException {

    }

    @Override
    public void deserialize(ByteBuffer byteBuffer) {

    }

    @Override
    public Integer getMinValue() {
        return null;
    }

    @Override
    public Integer getMaxValue() {
        return null;
    }

    @Override
    public Integer getFirstValue() {
        return null;
    }

    @Override
    public Integer getLastValue() {
        return null;
    }


    @Override
    public double getSumDoubleValue() {
        return 0;
    }

    @Override
    public long getSumLongValue() {
        return 0;
    }
}
