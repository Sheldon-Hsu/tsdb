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
package com.tsdb.jdbc.common.utils;

import com.tsdb.jdbc.common.data.DataType;
import com.tsdb.jdbc.common.data.Record;
import com.tsdb.jdbc.common.data.Records;

import static com.tsdb.jdbc.common.config.TSDBConstant.STRING_CHARSET;

public class RecordUtils {

    public static Records readRecordsFromBytes(byte[] bytes) {
        Records records = new Records();

        return records;
    }

    public static byte[] writeRecordsToBytes(Records records) {
        String[] names = records.getNames();
        DataType[] types = records.getTypes();
        Record[] recordArray = records.getRecords();
        int columnLength = names.length;
        Object[] nameBytes = new Object[columnLength];
        byte[] typeBytes = new byte[4 * columnLength];
        for (int i = 0; i < columnLength; i++) {
            String name = names[i];
            byte[] bytes = name.getBytes(STRING_CHARSET);
            nameBytes[i] = bytes;
            DataType type = types[i];
            typeBytes[i] = type.getCode().byteValue();

        }


//        1.column size
//        2.name byte size
//        3.datatype size = 4 * column size


//        ByteBuffer byteBuffer = new ByteBuffer();
        return null;
    }
}
