package com.tsdb.tsfile.file.header.statistics.value;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class BooleanStatistics extends ValueStatistics<Boolean> {
    @Override
    public void deserialize(InputStream inputStream) throws IOException {

    }

    @Override
    public void deserialize(ByteBuffer byteBuffer) {

    }

    @Override
    public Boolean getMinValue() {
        return null;
    }

    @Override
    public Boolean getMaxValue() {
        return null;
    }

    @Override
    public Boolean getFirstValue() {
        return null;
    }

    @Override
    public Boolean getLastValue() {
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
