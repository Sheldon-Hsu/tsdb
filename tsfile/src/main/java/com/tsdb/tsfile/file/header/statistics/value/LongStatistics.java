package com.tsdb.tsfile.file.header.statistics.value;

import com.tsdb.common.data.DataType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class LongStatistics extends ValueStatistics<Long> {
    private boolean isEmpty = true;
    private long min;
    private long max;
    /*
        value may be null
     */
    private Long first;
    private Long last;
    private int count = 0;
    private int noNullCount = 0;


    public LongStatistics() {
        dataType = DataType.BIGINT;
    }

    @Override
    public void deserialize(InputStream inputStream) throws IOException {

    }

    @Override
    public void deserialize(ByteBuffer byteBuffer) {

    }

    @Override
    public Long getMinValue() {
        return min;
    }

    @Override
    public Long getMaxValue() {
        return max;
    }

    @Override
    public Long getFirstValue() {
        return first;
    }

    @Override
    public Long getLastValue() {
        return last;
    }


    @Override
    public double getSumDoubleValue() {
        return doubleSum;
    }

    @Override
    public long getSumLongValue() {
        return longSum;
    }

    @Override
    public void update(Long value) {
        if (value != null) {
            noNullCount++;
            if (isEmpty) {
                min = value;
                max = value;
                first = value;
                isEmpty = false;
            } else {
                min = Math.min(min, value);
                max = Math.max(max, value);
            }
        } else {
            if (isEmpty) {
                first = value;
                isEmpty = false;
            }
        }
        count++;
        last = value;
        doubleSum += value;
        longSum += value;
    }
}
