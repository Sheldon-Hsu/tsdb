package com.tsdb.tsfile.file.header.statistics;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ChunkStatistics extends FileStatistics{



    @Override
    public int serialize(InputStream inputStream) throws IOException {
        return 0;
    }

    @Override
    public void serialize(ByteBuffer byteBuffer) {

    }

    @Override
    public void deserialize(InputStream inputStream) throws IOException {

    }

    @Override
    public void deserialize(ByteBuffer byteBuffer) {

    }
}
