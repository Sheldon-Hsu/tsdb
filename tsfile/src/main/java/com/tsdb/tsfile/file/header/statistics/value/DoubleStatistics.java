package com.tsdb.tsfile.file.header.statistics.value;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DoubleStatistics extends ValueStatistics<Double> {
    @Override
    public void deserialize(InputStream inputStream) throws IOException {

    }

    @Override
    public void deserialize(ByteBuffer byteBuffer) {

    }

    @Override
    public Double getMinValue() {
        return null;
    }

    @Override
    public Double getMaxValue() {
        return null;
    }

    @Override
    public Double getFirstValue() {
        return null;
    }

    @Override
    public Double getLastValue() {
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
