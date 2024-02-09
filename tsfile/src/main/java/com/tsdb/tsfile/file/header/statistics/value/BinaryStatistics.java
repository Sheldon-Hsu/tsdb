package com.tsdb.tsfile.file.header.statistics.value;

import com.tsdb.common.data.Binary;
import com.tsdb.common.data.DataType;
import com.tsdb.tsfile.exception.filter.StatisticsClassException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class BinaryStatistics extends ValueStatistics<Binary> {
    private boolean isEmpty = true;

    /*
        value may be null
     */
    private Binary first;
    private Binary last;
    private int count = 0;
    private int noNullCount = 0;

    public BinaryStatistics() {
        dataType = DataType.BINARY;
    }

    @Override
    public void deserialize(InputStream inputStream) throws IOException {

    }

    @Override
    public void deserialize(ByteBuffer byteBuffer) {

    }

    @Override
    public Binary getMinValue() {
        throw new StatisticsClassException(
                String.format(STATS_UNSUPPORTED_MSG, DataType.ARRAY, "min value "));
    }

    @Override
    public Binary getMaxValue() {
        throw new StatisticsClassException(
                String.format(STATS_UNSUPPORTED_MSG, DataType.ARRAY, "max value "));
    }

    @Override
    public Binary getFirstValue() {
        return first;
    }

    @Override
    public Binary getLastValue() {
        return last;
    }

    @Override
    public double getSumDoubleValue() {
        throw new StatisticsClassException(
                String.format(STATS_UNSUPPORTED_MSG, DataType.ARRAY, "double sum"));
    }

    @Override
    public long getSumLongValue() {
        throw new StatisticsClassException(
                String.format(STATS_UNSUPPORTED_MSG, DataType.ARRAY, "long sum"));
    }

    @Override
    public void update(Binary value) {
        if (value != null) {
            noNullCount++;
            if (isEmpty) {
                first = value;
                isEmpty = false;
            }
        } else {
            if (isEmpty) {
                first = value;
                isEmpty = false;
            }
        }
        count++;
        last = value;
    }
}
