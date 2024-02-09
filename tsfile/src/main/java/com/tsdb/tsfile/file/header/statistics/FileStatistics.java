package com.tsdb.tsfile.file.header.statistics;

import com.tsdb.tsfile.file.header.statistics.value.ValueStatistics;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public abstract class FileStatistics {
    protected ValueStatistics<?>[] valueStatistics;
    protected long startTime = 0L;
    protected long endTime = 0L;

    public abstract int serialize(InputStream inputStream) throws IOException;

    public abstract void serialize(ByteBuffer byteBuffer);

    public abstract void deserialize(InputStream inputStream) throws IOException;

    public abstract void deserialize(ByteBuffer byteBuffer);

    public void update(int columnIndex, Object[] data) {
        ValueStatistics statistics = valueStatistics[columnIndex];
        for (Object datum : data) {
            statistics.update(datum);
        }
    }

    public void updateTime(long[] timestamps) {
        for (long timestamp : timestamps) {
            startTime = startTime == 0 ? timestamp : Math.min(startTime, timestamp);
            endTime = endTime == 0 ? timestamp : Math.max(endTime, timestamp);
        }
    }

}
