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

import com.tsdb.jdbc.common.data.Binary;
import com.tsdb.jdbc.common.data.DataType;
import com.tsdb.jdbc.common.data.Record;
import com.tsdb.jdbc.common.data.Records;
import com.tsdb.jdbc.common.exception.UnSupportedDataTypeException;
import com.tsdb.jdbc.common.io.BytesUtils;
import com.tsdb.jdbc.common.io.IOUtils;

import java.sql.Timestamp;
import java.util.Date;

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
        int columnSize = names.length;
        Object[] nameBytes = new Object[columnSize];
        byte[][] dataBytes = new byte[columnSize][];
        byte[] typeBytes = new byte[IOUtils.INT_LEN * columnSize];
        for (int i = 0; i < columnSize; i++) {
            String name = names[i];
            byte[] bytes = name.getBytes(STRING_CHARSET);
            nameBytes[i] = bytes;
            byte[] type = BytesUtils.intToBytes(types[i].getCode());
            System.arraycopy(type, 0, typeBytes, i * IOUtils.INT_LEN, IOUtils.INT_LEN);
        }

        for (int i = 0; i < recordArray.length; i++) {
            Record record = recordArray[i];
            Object[] raw = record.getRaw();
            for (int k = 0; k < columnSize; k++) {
                Object data = raw[k];
                byte[] bytes = readObject(data, types[k]);
                dataBytes[k] = bytes;
            }
        }
//        1.column size
//        2.name byte size
//        3.datatype size = 4 * column size
        byte[] columnSizeBytes = BytesUtils.intToBytes(columnSize);
        byte[] nameBytesSize = BytesUtils.intToBytes(nameBytes.length);

//        ByteBuffer byteBuffer = new ByteBuffer();
        return null;
    }

    private static byte[] readObject(Object data, DataType dataType) {
        switch (dataType) {
            case BOOLEAN:
                return BytesUtils.boolToBytes((Boolean) data);
            case SMALLINT:
                return BytesUtils.shortToBytes((Short) data);
            case INTEGER:
                return BytesUtils.intToBytes((Integer) data);
            case BIGINT:
                return BytesUtils.longToBytes((Long) data);
            case FLOAT:
                return BytesUtils.floatToBytes((Float) data);
            case DOUBLE:
                return BytesUtils.doubleToBytes((Double) data);
            case DATE:
                return BytesUtils.longToBytes(((Date) data).getTime());
            case TIMESTAMP:
                return BytesUtils.longToBytes(((Timestamp) data).getTime());
            case VARCHAR:
                byte[] strBytes = BytesUtils.stringToBytes((String) data);
                int length = strBytes.length;
                byte[] lengthBytes = BytesUtils.intToBytes(length);
                byte[] result = new byte[length + 4];
                System.arraycopy(lengthBytes, 0, result, 0, 4);
                System.arraycopy(strBytes, 0, result, 4, length);
                return result;
            case BINARY:
                return ((Binary) data).getValues();
            case ARRAY:
            default:
                throw new UnSupportedDataTypeException(String.format("%s is not support yet.", dataType));
        }
    }
}
