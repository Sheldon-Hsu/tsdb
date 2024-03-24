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

package com.tsdb.tsfile.meta;

import java.util.Objects;

import static com.tsdb.tsfile.meta.MetaConstant.DEFAULT_SCHEMA;

public class TableInfo {
    private String catalog;
    private String schema = DEFAULT_SCHEMA;
    private String table;


    public TableInfo(String catalog, String schema, String table) {
        this.catalog = catalog;
        this.schema = schema;
        this.table = table;
    }

    public String getCatalog() {
        return catalog;
    }



    public String getSchema() {
        return schema;
    }



    public String getTable() {
        return table;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableInfo tableInfo = (TableInfo) o;
        return Objects.equals(catalog, tableInfo.catalog) && Objects.equals(schema, tableInfo.schema) && Objects.equals(table, tableInfo.table);
    }

    @Override
    public int hashCode() {
        return Objects.hash(catalog, schema, table);
    }
}
