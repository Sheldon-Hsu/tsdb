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

package com.tsdb.server.memory;

import com.tsdb.server.plan.physics.InsertRowPlan;
import com.tsdb.server.plan.physics.InsertRowsPlan;
import com.tsdb.jdbc.tsfile.memory.AlignedTVList;
import com.tsdb.jdbc.tsfile.memory.WritableChunk;
import com.tsdb.jdbc.tsfile.meta.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class WMemStore implements IWMemStore {
    private static final Logger logger = LoggerFactory.getLogger(WMemStore.class);
    private final Map<Integer, WritableChunk> writableChunkMap;
    private final Table table;

    public WMemStore(Table table) {
        this.writableChunkMap = new HashMap<>();
        this.table = table;
    }

    @Override
    public void insert(InsertRowPlan insertRowPlan) {
        logger.info("WMemStore insert data...");
        Integer id = insertRowPlan.getId();
        if (!writableChunkMap.containsKey(id)) {
            writableChunkMap.put(id, new WritableChunk(new AlignedTVList(table.getDataTypesOrdered())));
        }
        writableChunkMap.get(id).write(insertRowPlan.getTime(),insertRowPlan.getValues());

    }

    @Override
    public void insert(InsertRowsPlan insertRowsPlan) {
        logger.info("WMemStore insert datas...");
    }

    @Override
    public long getMemSize() {
        return 0;
    }
}
