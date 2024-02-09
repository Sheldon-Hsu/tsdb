package com.tsdb.tsfile.file.header.statistics.value;

import com.tsdb.common.data.DataType;
import com.tsdb.tsfile.exception.filter.StatisticsClassException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class StringStatistics extends ValueStatistics<String> {
    private boolean isEmpty = true;

    /*
        value may be null
     */
    private String first;
    private String last;
    private int count = 0;
    private int noNullCount = 0;

    public StringStatistics() {
        dataType = DataType.VARCHAR;
    }

    @Override
    public void deserialize(InputStream inputStream) throws IOException {

    }

    @Override
    public void deserialize(ByteBuffer byteBuffer) {

    }

    @Override
    public String getMinValue() {
        throw new StatisticsClassException(
                String.format(STATS_UNSUPPORTED_MSG, DataType.VARCHAR, "min value "));
    }

    @Override
    public String getMaxValue() {
        throw new StatisticsClassException(
                String.format(STATS_UNSUPPORTED_MSG, DataType.VARCHAR, "max value "));
    }

    @Override
    public String getFirstValue() {
        return first;
    }

    @Override
    public String getLastValue() {
        return last;
    }

    @Override
    public double getSumDoubleValue() {
        throw new StatisticsClassException(
                String.format(STATS_UNSUPPORTED_MSG, DataType.VARCHAR, "double sum"));
    }

    @Override
    public long getSumLongValue() {
        throw new StatisticsClassException(
                String.format(STATS_UNSUPPORTED_MSG, DataType.VARCHAR, "long sum"));
    }

    @Override
    public void update(String value) {
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
