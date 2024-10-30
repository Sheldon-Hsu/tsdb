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

package com.tsdb.jdbc.server.storage.processor;

import com.tsdb.jdbc.common.config.TSDBConfig;
import com.tsdb.jdbc.common.config.TSDBDescriptor;
import com.tsdb.jdbc.server.exception.WriteProcessException;
import com.tsdb.jdbc.server.flush.FlushManager;
import com.tsdb.jdbc.server.plan.physics.InsertRowPlan;
import com.tsdb.jdbc.tsfile.meta.Table;
import com.tsdb.jdbc.tsfile.write.chunk.IChunkWriter;
import com.tsdb.jdbc.server.memory.IWMemStore;
import com.tsdb.jdbc.server.memory.WMemStore;
import com.tsdb.jdbc.server.storage.StorageEngine;
import com.tsdb.jdbc.server.storage.TsFileResource;
import com.tsdb.jdbc.server.storage.WMSManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TsFileProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(StorageEngine.class);

    private static final TSDBConfig config = TSDBDescriptor.getInstance().getConfig();
    private static final WMSManager workMemManager = WMSManager.getInstance();
    private static final FlushManager flushManager = FlushManager.getInstance();
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    private TsFileResource tsFileResource;
    private IChunkWriter writer;
    private IWMemStore workMemStore;
    private long timeRangeId;
    private Long wMemStoreLastUpdateTime;
    private Table table;

    /**
     * Data is written to memory
     *
     private TsFileResource tsFileResource;
     private IChunkWriter writer;
     private IWMemStore workMemStore;
     * @param insertRowPlan
     */
    @Override
    public void insert(InsertRowPlan insertRowPlan) throws WriteProcessException {
        if (workMemStore == null) {
            workMemStore = new WMemStore(table);
        }
        String catalog = insertRowPlan.getCatalog();
        String schema = insertRowPlan.getSchema();
        String table = insertRowPlan.getTable();
        workMemStore = workMemManager.getAvailableMemStore(catalog, schema, table);
        rwLock.writeLock().lock();
        workMemStore.insert(insertRowPlan);
        rwLock.writeLock().unlock();
        wMemStoreLastUpdateTime = System.currentTimeMillis();
        tryFlush();
    }


    private void tryFlush() {
        if (workMemStore.getMemSize() > config.getWorkMemSize() || distanceFromLastUpdate() > config.getLongestFlushTime()) {
            flush();
        }
    }

    private void flush() {
        rwLock.writeLock().lock();
        flushManager.addToFlush(workMemStore);
        workMemStore = new WMemStore(table);
        rwLock.writeLock().unlock();
    }

    private long distanceFromLastUpdate() {
        return System.currentTimeMillis() - wMemStoreLastUpdateTime;
    }

    public TsFileResource getTsFileResource() {
        return tsFileResource;
    }

    public void setTsFileResource(TsFileResource tsFileResource) {
        this.tsFileResource = tsFileResource;
    }
}
