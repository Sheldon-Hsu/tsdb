package com.tsdb.tsfile.file.header.statistics.value;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class LongStatistics extends ValueStatistics<Long>{
    @Override
    public void deserialize(InputStream inputStream) throws IOException {

    }

    @Override
    public void deserialize(ByteBuffer byteBuffer) {

    }

    @Override
    public Long getMinValue() {
        return null;
    }

    @Override
    public Long getMaxValue() {
        return null;
    }

    @Override
    public Long getFirstValue() {
        return null;
    }

    @Override
    public Long getLastValue() {
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
