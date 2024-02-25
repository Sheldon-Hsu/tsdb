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

import java.util.ArrayList;
import java.util.List;

public class AlignedTVList extends TVList{
    public AlignedTVList(DataType[] columnDataType) {
        super(columnDataType);
        this.timestamps = new ArrayList<>();
    }

    /**
     * Arrays can save memory space, but the length cannot be specified when initializing.
     * Using list<long[]> can initialize a fixed-length array,
     * which can be automatically expanded by the list.add method if the array is insufficient.
     * However, list is also dynamically expanded by copying arrays, which can be modified to achieve dynamic expansion of arrays.
     */
    protected List<long[]> timestamps;

    public List<long[]> getTimestamps(){
        return timestamps;
    }

    @Override
    public void put(long timestamp, Object[] lineValue) {
        long floatLength = 0L;
        int arrayIndex = rowCount / ARRAY_SIZE;
        int elementIndex = rowCount % ARRAY_SIZE;
        if (arrayIndex >= timestamps.size()) {
            timestamps.add(new long[ARRAY_SIZE]);
            values.add(new Object[ARRAY_SIZE][columnSize]);
        }
        timestamps.get(arrayIndex)[elementIndex] = timestamp;
        values.get(arrayIndex)[elementIndex] = lineValue;
        for (int i = 0; i < lineValue.length; i++) {
            if (lengthFloatData.containsKey(i)) {
                DataType dataType = lengthFloatData.get(i);
                switch (dataType) {
                    case VARCHAR:
                        floatLength += StringUtil.calcLength((String) lineValue[i]);
                    default:
                }
            }
        }

        maxTime = Math.max(maxTime, timestamp);
        minTime = rowCount == 0 ? timestamp : Math.min(minTime, timestamp);
        rowCount++;
        length += fixedLength;
        length += floatLength;
    }

}
