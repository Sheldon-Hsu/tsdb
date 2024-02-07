package com.tsdb.tsfile.file.header;

import com.tsdb.tsfile.file.header.statistics.ChunkStatistics;

public class ChunkHeader {
    private int dataSize;
    private int numOfPages;
    private ChunkStatistics chunkStatistics;

    public ChunkHeader(int dataSize, int numOfPages, ChunkStatistics chunkStatistics) {
        this.dataSize = dataSize;
        this.numOfPages = numOfPages;
        this.chunkStatistics = chunkStatistics;
    }
}
