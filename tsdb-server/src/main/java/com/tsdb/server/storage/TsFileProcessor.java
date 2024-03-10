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

import com.tsdb.server.exception.WriteProcessException;
import com.tsdb.server.memory.IWMemStore;
import com.tsdb.server.memory.WMenStore;
import com.tsdb.server.plan.physics.InsertRowPlan;
import com.tsdb.tsfile.meta.MetaConstant;
import com.tsdb.tsfile.write.chunk.IChunkWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TsFileProcessor {
    private static final Logger logger = LoggerFactory.getLogger(StorageEngine.class);
    private static final WMSManager workMemManager = WMSManager.getInstance();

    private TsFileResource tsFileResource;
    private IChunkWriter writer;
    private IWMemStore workMemStore;
    private long timeRangeId;

    /**
     * Data is written to memory
     * @param insertRowPlan
     */
    public void insert(InsertRowPlan insertRowPlan) throws WriteProcessException {
        if (workMemStore == null){
            workMemStore = new WMenStore();
        }else {
            String catalog = insertRowPlan.getCatalog();
            String schema = insertRowPlan.getSchema();
            MetaConstant.OBJECT obj = insertRowPlan.getObject();
            if (MetaConstant.OBJECT.TABLE.equals(obj)){
                String table = insertRowPlan.getTable();
                workMemManager.getAvailableMemStore(catalog,schema,table);
            }else if (MetaConstant.OBJECT.VIEW.equals(obj)){
                String view = insertRowPlan.getView();
                workMemManager.getAvailableMemStore(catalog,schema,view);
            }else {
                throw new WriteProcessException("");
            }

        }
    }


}
