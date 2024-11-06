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

package com.tsdb.server.storage;

import com.tsdb.server.concurrent.threadpool.StoreTaskPoolManager;
import com.tsdb.server.exception.InsertObjectTypeException;
import com.tsdb.server.exception.WriteProcessException;
import com.tsdb.server.flush.FlushManager;
import com.tsdb.server.plan.physics.InsertRowPlan;
import com.tsdb.server.plan.physics.InsertRowsPlan;
import com.tsdb.server.plan.physics.PhysicalPlan;
import com.tsdb.server.storage.processor.RegionProcessor;
import com.tsdb.jdbc.tsfile.exception.write.DiskSpaceInsufficientException;
import com.tsdb.jdbc.tsfile.meta.TableInfo;
import com.tsdb.server.service.IService;
import com.tsdb.server.service.ServiceID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class StorageEngine implements IService {
    private static final Logger logger = LoggerFactory.getLogger(StorageEngine.class);

    private FlushManager flushManager = FlushManager.getInstance();
    private WMSManager wmsmanager = WMSManager.getInstance();

    private Map<TableInfo, RegionProcessor> processorMap = new ConcurrentHashMap<>();
    private static long timePartitionInterval = -1;
    private StoreTaskPoolManager pool = StoreTaskPoolManager.getInstance();
    /**
     * Write a piece of data. Data is not written to the disk for the time being.
     * It is stored in memory until the appropriate time is reached before it is written to the file.
     * WAL is used to save the write record when writing.
     *
     * @param insertPlan
     */
    public void insert(InsertRowPlan insertPlan) throws WriteProcessException, InsertObjectTypeException, IOException, DiskSpaceInsufficientException {
        logger.info("StorageEngine insert data...");
        TableInfo table = insertPlan.getTableInfo();
        RegionProcessor processor = getProcessor(table);
        processor.insert(insertPlan);
    }

    public void batchInsert(InsertRowsPlan insertPlan) {
        logger.info("StorageEngine batch insert data...");

    }


    public void query(PhysicalPlan queryPlan) {
        logger.info("StorageEngine query data...");
    }


    private RegionProcessor getProcessor(TableInfo tableInfo) {
        return processorMap.get(tableInfo);
    }


    public static long getTimePartition(long time) {
        return  time / timePartitionInterval ;
    }

    private static void initTimePartition() {

    }


    @Override
    public void start() {
        logger.info("StorageEngine start...");
        pool.start();
        initTimePartition();
    }

    @Override
    public void stop() {
        logger.info("StorageEngine stop...");
    }


    @Override
    public void shutdownNow() {

    }

    @Override
    public ServiceID getServiceID() {
        return ServiceID.STORAGE_ENGINE_SERVICE;
    }


    public static StorageEngine getInstance() {
        return InstanceHolder.INSTANCE;
    }

    static class InstanceHolder {

        private static final StorageEngine INSTANCE = new StorageEngine();

        private InstanceHolder() {
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StorageEngine that = (StorageEngine) o;
        return Objects.equals(flushManager, that.flushManager) && Objects.equals(wmsmanager, that.wmsmanager) && Objects.equals(processorMap, that.processorMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flushManager, wmsmanager, processorMap);
    }
}
