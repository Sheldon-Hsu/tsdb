package com.tsdb.tsfile.file.header.statistics;

import com.tsdb.tsfile.file.header.statistics.value.ValueStatistics;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

public abstract class FileStatistics {
    protected ValueStatistics<?>[] valueStatistics;
    protected long startTime;
    protected long endTime;

    public abstract int serialize(InputStream inputStream) throws IOException;
    public abstract void serialize(ByteBuffer byteBuffer);

    public abstract void deserialize(InputStream inputStream) throws IOException;
    public abstract void deserialize(ByteBuffer byteBuffer);

}
