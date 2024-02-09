package com.tsdb.tsfile.file.header.statistics.value;

import com.tsdb.common.data.DataType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DoubleStatistics extends ValueStatistics<Double> {

    private boolean isEmpty = true;
    private double min;
    private double max;
    /*
        value may be null
     */
    private Double first;
    private Double last;
    private int count = 0;
    private int noNullCount = 0;

    public DoubleStatistics() {
        dataType = DataType.DOUBLE;
    }

    @Override
    public void deserialize(InputStream inputStream) throws IOException {

    }

    @Override
    public void deserialize(ByteBuffer byteBuffer) {

    }

    @Override
    public Double getMinValue() {
        return min;
    }

    @Override
    public Double getMaxValue() {
        return max;
    }

    @Override
    public Double getFirstValue() {
        return first;
    }

    @Override
    public Double getLastValue() {
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
    public void update(Double value) {
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
