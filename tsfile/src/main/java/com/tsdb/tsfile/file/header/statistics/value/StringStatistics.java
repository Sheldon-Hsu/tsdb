package com.tsdb.tsfile.file.header.statistics.value;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class StringStatistics extends ValueStatistics<String> {
    @Override
    public void deserialize(InputStream inputStream) throws IOException {

    }

    @Override
    public void deserialize(ByteBuffer byteBuffer) {

    }

    @Override
    public String getMinValue() {
        return null;
    }

    @Override
    public String getMaxValue() {
        return null;
    }

    @Override
    public String getFirstValue() {
        return null;
    }

    @Override
    public String getLastValue() {
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
