package com.tsdb.tsfile.file.header.statistics.value;

import com.tsdb.common.data.DataType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class IntegerStatistics extends ValueStatistics<Integer> {
    private boolean isEmpty = true;
    private int min;
    private int max;
    /*
        value may be null
     */
    private Integer first;
    private Integer last;
    private int count = 0;
    private int noNullCount = 0;


    public IntegerStatistics() {
        dataType = DataType.INTEGER;
    }

    @Override
    public void deserialize(InputStream inputStream) throws IOException {

    }

    @Override
    public void deserialize(ByteBuffer byteBuffer) {

    }

    @Override
    public Integer getMinValue() {
        return min;
    }

    @Override
    public Integer getMaxValue() {
        return max;
    }

    @Override
    public Integer getFirstValue() {
        return first;
    }

    @Override
    public Integer getLastValue() {
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
    public void update(Integer value) {
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
