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

package com.tsdb.jdbc.server.storage;


import com.tsdb.jdbc.common.config.TSDBConfig;
import com.tsdb.jdbc.common.config.TSDBDescriptor;
import com.tsdb.jdbc.server.exception.WriteProcessException;
import com.tsdb.jdbc.server.storage.processor.TsFileProcessor;
import com.tsdb.jdbc.tsfile.meta.MetaConstant;
import com.tsdb.jdbc.tsfile.meta.Table;
import com.tsdb.jdbc.server.memory.IWMemStore;
import com.tsdb.jdbc.server.memory.WMemStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WMSManager {
    private static final TSDBConfig config = TSDBDescriptor.getInstance().getConfig();
    private static final Logger logger = LoggerFactory.getLogger(WMSManager.class);
    private int currentMemStoreNumber = 0; 
    private static final int WAIT_TIME = 100;
    private final Map<String, Map<MetaConstant.ObjectType, TsFileProcessor>> cache = new ConcurrentHashMap<>();

    private Table table;

    public TsFileProcessor getProcessor(String catalog, MetaConstant.ObjectType type, String name) {
        TsFileProcessor processor = cache.get(catalog).get(type);
        return processor;
    }


    public synchronized IWMemStore getAvailableMemStore(String catalog, String schema, String tableName) throws WriteProcessException {
        if (!reachMaxMemStoreNumber()) {
            addMemStoreNumber();
            return new WMemStore(table);
        }
        int waitCount = 1;
        while (true) {
            if (!reachMaxMemStoreNumber()) {
                addMemStoreNumber();
                return new WMemStore(table);
            }
            try {
                wait(WAIT_TIME);
            } catch (InterruptedException e) {
                logger.error("{}.{}.{} fails to wait for memstore {}, continue to wait", catalog, schema, table, e);
                Thread.currentThread().interrupt();
                throw new WriteProcessException(e);
            }
            if (waitCount++ % 10 == 0) {
                logger.info("{}.{}.{} has waited for a memtable for {}ms", catalog, schema, table, waitCount * WAIT_TIME);
            }
        }
    }

    public synchronized void addMemStoreNumber() {
        currentMemStoreNumber++;
    }


    public synchronized void decreaseMemStoreNumber() {
        currentMemStoreNumber--;
        notifyAll();
    }

    private boolean reachMaxMemStoreNumber() {
        return currentMemStoreNumber >= config.getMaxMemStoreNumber();
    }


    public static WMSManager getInstance() {
        return WmsManagerHolder.INSTANCE;
    }

    private static class WmsManagerHolder {

        private WmsManagerHolder() {
            // allowed to do nothing
        }

        private static final WMSManager INSTANCE = new WMSManager();
    }
}
