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


import com.tsdb.common.config.TSDBConfig;
import com.tsdb.common.config.TSDBDescriptor;
import com.tsdb.server.exception.WriteProcessException;
import com.tsdb.server.memory.IWMemStore;
import com.tsdb.server.memory.WMenStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WMSManager {
    private static final TSDBConfig config =  TSDBDescriptor.getInstance().getConfig();
    private static final Logger logger = LoggerFactory.getLogger(WMSManager.class);
    private int currentMemStoreNumber = 0;
    private static final int WAIT_TIME = 100;

    public TsFileProcessor getProcessor(){

        return new TsFileProcessor();
    }


    public synchronized IWMemStore getAvailableMemStore(String catalog,String schema,String table) throws WriteProcessException {
        if (!reachMaxMemStoreNumber()){
            addMemStoreNumber();
            return new WMenStore();
        }
        int waitCount = 1;
        while (true){
            if (!reachMaxMemStoreNumber()) {
                addMemStoreNumber();
                return new WMenStore();
            }
            try {
                wait(WAIT_TIME);
            } catch (InterruptedException e) {
                logger.error("{}.{}.{} fails to wait for memstore {}, continue to wait", catalog, schema,table,e);
                Thread.currentThread().interrupt();
                throw new WriteProcessException(e);
            }
            if (waitCount++ % 10 == 0) {
                logger.info("{}.{}.{} has waited for a memtable for {}ms", catalog, schema,table, waitCount * WAIT_TIME);
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
