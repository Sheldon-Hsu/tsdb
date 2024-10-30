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

package com.tsdb.jdbc.server.plan.physics;

import com.tsdb.jdbc.tsfile.meta.MetaConstant;
import com.tsdb.jdbc.tsfile.meta.TableInfo;

public class InsertRowPlan implements PhysicalPlan{
    private TableInfo tableInfo ;
    private long time;
    private int id;
    private Object[] values;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTable(){
        return tableInfo.getTable();
    }

    @Override
    public TableInfo getTableInfo() {
        return null;
    }

    public String getView(){
        return null;
    }

    @Override
    public String getCatalog() {
        return tableInfo.getCatalog();
    }

    @Override
    public String getSchema() {
        return tableInfo.getSchema();
    }

    @Override
    public MetaConstant.ObjectType getObject() {
        return null;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }
}
