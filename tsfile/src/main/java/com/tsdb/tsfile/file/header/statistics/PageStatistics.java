package com.tsdb.tsfile.file.header.statistics;

import com.tsdb.common.data.Binary;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class PageStatistics extends FileStatistics {

    private Binary spatialSign;


    public PageStatistics() {
    }

    public Binary getSpatialSign() {
        return spatialSign;
    }

    public void setSpatialSign(Binary spatialSign) {
        this.spatialSign = spatialSign;
    }


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
