package com.tsdb.tsfile.file.header.statistics.value;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class FloatStatistics extends ValueStatistics<Float> {
    @Override
    public void deserialize(InputStream inputStream) throws IOException {

    }

    @Override
    public void deserialize(ByteBuffer byteBuffer) {

    }

    @Override
    public Float getMinValue() {
        return null;
    }

    @Override
    public Float getMaxValue() {
        return null;
    }

    @Override
    public Float getFirstValue() {
        return null;
    }

    @Override
    public Float getLastValue() {
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
