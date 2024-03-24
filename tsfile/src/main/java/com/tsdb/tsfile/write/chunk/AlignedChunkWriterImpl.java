/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tsdb.tsfile.write.chunk;

import com.tsdb.tsfile.meta.Table;
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

    AlignedChunkWriterImpl(Table table){
        this.pageWriter = new PageWriter(table);
    }

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
