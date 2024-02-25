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

package com.tsdb.tsfile.memory;

import com.tsdb.common.data.DataType;
import com.tsdb.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Time series data memory model, for write.
 */
public abstract class TVList {
    private static final Logger LOGGER = LoggerFactory.getLogger(TVList.class);

    public static final int ARRAY_SIZE = 1024;

    //    todo Try to use basic types instead of boxed type for efficiency
    protected List<Object[][]> values;

    /**
     * Used to calculate the memory size
     */
    protected long length;
    protected int fixedLength;
    protected final Map<Integer, DataType> lengthFloatData = new HashMap<>();
    protected int rowCount;
    protected long maxTime;
    protected long minTime;
    protected int columnSize;
    protected DataType[] columnDataType;

    public TVList(DataType[] columnDataType) {

        this.values = new ArrayList<>();
        this.columnDataType = columnDataType;
        this.columnSize = columnDataType.length;
        this.rowCount = 0;
        calcFixedLength();
    }

    /**
     * Add a record where the data is presented as a row in memory and converted to a column when flushed into the file.
     * Row data structures are easier for developers to understand.
     *
     * @param timestamp
     * @param lineValue
     */
    abstract void put(long timestamp, Object[] lineValue);

    public int getRowCount() {
        return rowCount;
    }

    public long getDataLength() {
        return length;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public long getMinTime() {
        return minTime;
    }

    /**
     * Calculate the fixed length of a row. The fixed length does not change with the row data.
     */
    private void calcFixedLength() {
        for (int i = 0; i < columnDataType.length; i++) {
            DataType dataType = columnDataType[i];
            int columnFixLength = dataType.getFixLength();
            if (columnFixLength > 0) {
                fixedLength += columnFixLength;
            } else {
                lengthFloatData.put(i, dataType);
            }
        }
    }

    public abstract List<long[]> getTimestamps();

    public List<Object[][]> getValues() {
        return values;
    }
}
