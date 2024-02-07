package com.tsdb.tsfile.file.header.statistics.value;

import com.tsdb.common.data.Binary;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class BinaryStatistics extends ValueStatistics<Binary> {
    @Override
    public void deserialize(InputStream inputStream) throws IOException {

    }

    @Override
    public void deserialize(ByteBuffer byteBuffer) {

    }

    @Override
    public Binary getMinValue() {
        return null;
    }

    @Override
    public Binary getMaxValue() {
        return null;
    }

    @Override
    public Binary getFirstValue() {
        return null;
    }

    @Override
    public Binary getLastValue() {
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
