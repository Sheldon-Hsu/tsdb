package com.tsdb.tsfile.file.header;

public class ChunkStatistics {
    private int dataSize;
    private int numOfPages;
    private ChunkStatistics chunkStatistics;

    public ChunkStatistics(int dataSize, int numOfPages, ChunkStatistics chunkStatistics) {
        this.dataSize = dataSize;
        this.numOfPages = numOfPages;
        this.chunkStatistics = chunkStatistics;
    }
}
