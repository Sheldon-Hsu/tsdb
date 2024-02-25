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

    /**
     * An easy way to understand datatype structure is List<Object>[],
     * where each element in the array represents all the data for a column,
     * but this requires multiple calculations as the List is expanded,
     * so use List<Object[]> for efficiency,where each element of the List is a two-dimensional array.
     * The array length is N * 1024,N is column number and 1024 is the fixed length for each extension.
     * Object[] represent two-dimensional arrays in order to avoid the use of boxed types, to save memory
     */
    protected List<Object[]> values;

    /**
     * Used to calculate the memory size
     */
    protected long length;
    /**
     * A row of data is divided into two types: fixed length and floating length.
     * The numeric type is fixed length, such as int float,
     * and the character type is floating length, such as varchar.
     * The length is calculated here to better control memory usage
     */
    protected int fixedLength;
    protected final Map<Integer, DataType> lengthFloatData = new HashMap<>();
    protected int rowCount;
    protected long maxTime;
    protected long minTime;
    protected int columnSize;
    protected DataType[] columnDataType;

    public TVList(DataType[] columnDataType) {


        this.columnDataType = columnDataType;
        this.columnSize = columnDataType.length;
        this.values = new ArrayList<>(1024);

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

    public List<Object[]> getValues() {
        return values;
    }
}
