package com.tsdb.tsfile.write.chunk;

import com.tsdb.tsfile.write.page.PageWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Aligned ChunkWriter, the same record is generated at the same time for each field
 */
public class AlignedChunkWriterImpl implements IChunkWriter {
    private static final Logger logger = LoggerFactory.getLogger(AlignedChunkWriterImpl.class);
    private PageWriter pageWriter;

    @Override
    public void writeToFileWriter() throws IOException {

    }

    @Override
    public long estimateMaxSeriesMemSize() {
        return 0;
    }

    @Override
    public long getSerializedChunkSize() {
        return 0;
    }

    @Override
    public void sealCurrentPage() {

    }

    @Override
    public void clearPageWriter() {

    }

    @Override
    public boolean checkIsChunkSizeOverThreshold(long size, long pointNum, boolean returnTrueIfChunkEmpty) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean checkIsUnsealedPageOverThreshold(long size, long pointNum, boolean returnTrueIfPageEmpty) {
        return false;
    }
}
