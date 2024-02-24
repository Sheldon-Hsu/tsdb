
package com.tsdb.tsfile.write.chunk;


import java.io.IOException;

/**
 * IChunkWriter provides a list of writing methods for different value types.
 */
public interface IChunkWriter {

    /**
     * flush data to TsFileIOWriter.
     */
    void writeToFileWriter() throws IOException;

    /**
     * estimate memory usage of this series.
     */
    long estimateMaxSeriesMemSize();

    /**
     * return the serialized size of the chunk header + all pages (not including the un-sealed page).
     * Notice, call this method before calling writeToFileWriter(), otherwise the page buffer in
     * memory will be cleared. <br>
     * If there is no data points in the chunk, return 0 (i.e., in this case, the size of header is
     * not calculated, because nothing will be serialized latter)</>
     */
    long getSerializedChunkSize();

    /**
     * seal the current page which may has not enough data points in force.
     */
    void sealCurrentPage();

    /**
     * set the current pageWriter to null, friendly for gc
     */
    void clearPageWriter();

    /**
     * used for compaction to check whether the chunk is over threshold or not. Return true if there
     * is no unsealed chunk.
     */
    boolean checkIsChunkSizeOverThreshold(long size, long pointNum, boolean returnTrueIfChunkEmpty);

    /**
     * Return true if the chunk writer is empty
     */
    boolean isEmpty();

    /**
     * used for compaction to check whether the unsealed page is over threshold or not. Return true if
     * there is no unsealed page.
     */
    boolean checkIsUnsealedPageOverThreshold(long size, long pointNum, boolean returnTrueIfPageEmpty);
}
