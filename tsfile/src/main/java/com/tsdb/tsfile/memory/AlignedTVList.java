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

import com.tsdb.common.config.TSDBConstant;
import com.tsdb.common.data.Binary;
import com.tsdb.common.data.DataType;
import com.tsdb.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import static com.tsdb.common.config.TSDBConstant.OBJECT_HEADER_SIZE;

public class AlignedTVList extends TVList {
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

    public List<long[]> getTimestamps() {
        return timestamps;
    }

    @Override
    public void put(long timestamp, Object[] lineValue) {
        long floatLength = 0L;
        int arrayIndex = rowCount / ARRAY_SIZE;
        int elementIndex = rowCount % ARRAY_SIZE;
        if (arrayIndex >= timestamps.size()) {
            timestamps.add(new long[ARRAY_SIZE]);
            Object[] data = new Object[columnSize];
            for (int i = 0; i < columnDataType.length; i++) {
                switch (columnDataType[i]) {
                    case BOOLEAN:
                        data[i] = new boolean[ARRAY_SIZE];
                        break;
                    case SMALLINT:
                        data[i] = new short[ARRAY_SIZE];
                        break;
                    case INTEGER:
                        data[i] = new int[ARRAY_SIZE];
                        break;
                    case FLOAT:
                        data[i] = new float[ARRAY_SIZE];
                        break;
                    case DOUBLE:
                        data[i] = new double[ARRAY_SIZE];
                        break;
                    case BIGINT:
                    case DATE:
                    case TIMESTAMP:
                        data[i] = new long[ARRAY_SIZE];
                        break;
                    case VARCHAR:
                    case BINARY:
                    case ARRAY:
                        data[i] = new byte[ARRAY_SIZE][];
                        break;
                }
            }
            values.add(data);
        }
        timestamps.get(arrayIndex)[elementIndex] = timestamp;
        for (int i = 0; i < columnDataType.length; i++) {

            switch (columnDataType[i]) {
                case BOOLEAN:
                    ((boolean[])(values.get(arrayIndex)[i]))[elementIndex] = (boolean) lineValue[i];
                    break;
                case SMALLINT:
                    ((short[])(values.get(arrayIndex)[i]))[elementIndex] = (short) lineValue[i];
                    break;
                case INTEGER:
                    ((int[])(values.get(arrayIndex)[i]))[elementIndex] = (int) lineValue[i];
                    break;
                case FLOAT:
                    ((float[])(values.get(arrayIndex)[i]))[elementIndex] = (float) lineValue[i];
                    break;
                case DOUBLE:
                    ((double[])(values.get(arrayIndex)[i]))[elementIndex] = (double) lineValue[i];
                    break;
                case BIGINT:
                case DATE:
                case TIMESTAMP:
                    ((long[])(values.get(arrayIndex)[i]))[elementIndex] = (long) lineValue[i];
                    break;
                case VARCHAR:
                    byte[] strBytes = ((String) lineValue[i]).getBytes(TSDBConstant.STRING_CHARSET);
                    ((byte[][])(values.get(arrayIndex)[i]))[elementIndex] = strBytes;
                    int strLength  = strBytes.length +  + OBJECT_HEADER_SIZE;
                    floatLength += aligned8(strLength);
                    break;
                case BINARY:
                    byte[] binaryBytes = ((Binary) lineValue[i]).getValues();
                    ((byte[][])(values.get(elementIndex)[i]))[elementIndex] =binaryBytes;
                    int binaryLength  = binaryBytes.length +  + OBJECT_HEADER_SIZE;
                    floatLength += aligned8(binaryLength);
                    break;
                case ARRAY:
//                    TODO The array type needs to get the type of the content and convert it to binary

                    break;
            }


        }

        maxTime = Math.max(maxTime, timestamp);
        minTime = rowCount == 0 ? timestamp : Math.min(minTime, timestamp);
        rowCount++;
        length += fixedLength;
        length += floatLength;
    }

    /**
     * object header aligned ,default 8.
     * @param size
     * @return
     */
    private int aligned8(int size){
        return size +8 >>3 << 3;
    }

}
