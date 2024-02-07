package com.tsdb.tsfile.file.header;

public class PageHeader {
    private int uncompressedSize;
    private int compressedSize;
    private final PageStatistics statistics;

    public PageHeader(int uncompressedSize, int compressedSize, PageStatistics statistics) {
        this.uncompressedSize = uncompressedSize;
        this.compressedSize = compressedSize;
        this.statistics = statistics;
    }
}
