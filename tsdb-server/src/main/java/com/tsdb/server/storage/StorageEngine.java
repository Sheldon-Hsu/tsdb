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

import com.tsdb.server.exception.InsertObjectTypeException;
import com.tsdb.server.exception.WriteProcessException;
import com.tsdb.server.flush.FlushManager;
import com.tsdb.server.plan.physics.InsertRowPlan;
import com.tsdb.server.plan.physics.InsertRowsPlan;
import com.tsdb.server.plan.physics.PhysicalPlan;
import com.tsdb.server.service.IService;
import com.tsdb.server.service.ServiceID;
import com.tsdb.server.storage.processor.Processor;
import com.tsdb.server.storage.processor.ViewProcessor;
import com.tsdb.tsfile.meta.MetaConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StorageEngine implements IService {
    private static final Logger logger = LoggerFactory.getLogger(StorageEngine.class);

    private FlushManager flushManager = FlushManager.getInstance();
    private WMSManager wmsmanager = WMSManager.getInstance();
    private Map<String, ViewProcessor> view = new ConcurrentHashMap<>();

    /**
     * Write a piece of data. Data is not written to the disk for the time being.
     * It is stored in memory until the appropriate time is reached before it is written to the file.
     * WAL is used to save the write record when writing.
     *
     * @param insertPlan
     */
    public void insert(InsertRowPlan insertPlan) throws WriteProcessException, InsertObjectTypeException {
        logger.info("StorageEngine insert data...");
        String catalog = insertPlan.getCatalog();
        MetaConstant.ObjectType type = insertPlan.getObject();
        String name;
        switch (type) {
            case TABLE:
                name = insertPlan.getTable();
                break;
            case VIEW:
                name = insertPlan.getView();
                break;
            default:
                throw new InsertObjectTypeException(String.format("object type: %s does not support writing",type));

        }
        Processor processor = getProcessor(catalog, type, name);
        processor.insert(insertPlan);
    }

    public void batchInsert(InsertRowsPlan insertPlan) {
        logger.info("StorageEngine batch insert data...");

    }


    public void query(PhysicalPlan queryPlan) {
        logger.info("StorageEngine query data...");
    }


    public Processor getProcessor(String catalog, MetaConstant.ObjectType type, String name) throws WriteProcessException {
        switch (type){
            case TABLE:
                return wmsmanager.getProcessor(catalog, type, name);
            case VIEW:
                String key = catalog+"."+name;
                if (view.containsKey(key)){
                    return view.get(key);
                }else {
                   return view.put(key,new ViewProcessor());
                }
            default:
                throw new WriteProcessException(String.format("no processor for object type: %s",type));
        }

    }


    @Override
    public void start() {
        logger.info("StorageEngine start...");
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

}
