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

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

import static com.tsdb.jdbc.common.config.TSDBConstant.STRING_CHARSET;

public class RecordUtils {


    public static byte[] writeRecordsToBytes(Records records) {
        String[] names = records.getNames();
        DataType[] types = records.getTypes();
        Record[] recordArray = records.getRecords();
        int columnSize = names.length;
        byte[][] nameBytes = new byte[columnSize][];
        byte[][] dataBytes = new byte[columnSize][];
        byte[] typeBytes = new byte[IOUtils.INT_LEN * columnSize];
        for (int i = 0; i < columnSize; i++) {
            String name = names[i];
            byte[] bytes = name.getBytes(STRING_CHARSET);
            byte[] length = BytesUtils.intToBytes(bytes.length);
            nameBytes[i] = new byte[4 + bytes.length];
            System.arraycopy(length, 0, nameBytes[i], 0, 4);
            System.arraycopy(bytes, 0, nameBytes[i], 4, bytes.length);
            byte[] type = BytesUtils.intToBytes(types[i].getCode());
            System.arraycopy(type, 0, typeBytes, i * IOUtils.INT_LEN, IOUtils.INT_LEN);
        }
        byte[] recordLength = BytesUtils.intToBytes(recordArray.length);
        for (int i = 0; i < recordArray.length; i++) {
            Record record = recordArray[i];
            Object[] raw = record.getRaw();
            for (int k = 0; k < columnSize; k++) {
                Object data = raw[k];
                byte[] bytes = writeObject(data, types[k]);
                dataBytes[k] = new byte[bytes.length];
                System.arraycopy(bytes, 0, dataBytes[k], 0, bytes.length);
            }
        }
//        1.column size
//        2.name byte size /datatype size = 4 * column size
//        3.name
//        4.data type
//        5.data
        int nameBytesSize = Arrays.stream(nameBytes).mapToInt(name -> name.length).sum();
        int dataBytesSize = Arrays.stream(dataBytes).mapToInt(data -> data.length).sum();
        byte[] columnSizeBytes = BytesUtils.intToBytes(columnSize);
        byte[] nameBytesSizeBytes = BytesUtils.intToBytes(nameBytesSize);
        byte[] dataBytesSizeBytes = BytesUtils.intToBytes(dataBytesSize);
        int totalSize = 4 +
                4 +
                nameBytesSize +
                typeBytes.length +
                4 +
                dataBytesSize;

        ByteBuffer byteBuffer = ByteBuffer.allocate(totalSize);
        byteBuffer.put(columnSizeBytes);
        byteBuffer.put(nameBytesSizeBytes);
        for (byte[] nameByte : nameBytes) {
            byteBuffer.put(nameByte);
        }

        byteBuffer.put(typeBytes);
        byteBuffer.put(dataBytesSizeBytes);
        for (byte[] dataByte : dataBytes) {
            byteBuffer.put(dataByte);
        }
        return byteBuffer.array();
    }

    public static Records readRecordsFromBytes(byte[] bytes) {
        Records records = new Records();
//        1.column size
//        2.name byte size /datatype size = 4 * column size
//        3.name
//        4.data type
//        5.data
        int offset = 0;
        int columnSize = BytesUtils.bytesToInt(bytes, offset);
        offset += 4;
        int nameSize = BytesUtils.bytesToInt(bytes, offset);
        offset += 4;
        byte[] nameBytes = new byte[nameSize];
        System.arraycopy(bytes, offset, nameBytes, 0, nameSize);
        offset += nameSize;
        byte[] dataType = new byte[columnSize * 4];
        System.arraycopy(bytes, offset, dataType, 0, columnSize * 4);
        offset += columnSize * 4;
        int dataBytesSizeBytes = BytesUtils.bytesToInt(bytes, offset);
        offset += 4;
        byte[] dataBytes = new byte[dataBytesSizeBytes];
        System.arraycopy(bytes, offset, dataBytes, 0, dataBytesSizeBytes);
        DataType[] dataTypes = new DataType[columnSize];
        for (int i = 0; i < columnSize; i++) {
            int typeCode = BytesUtils.bytesToInt(dataType, i * 4);
            dataTypes[i] = DataType.deserialize(typeCode);
        }
        int index = 0;
        ByteBuffer buffer = ByteBuffer.wrap(dataBytes);

        Record[] recordArray = new Record[buffer.getInt()];
        Record record = new Record();
        while (buffer.hasRemaining()) {
            int rawIndex = index % columnSize;
            if (rawIndex==0){
                record = new Record();
            }
            DataType type = dataTypes[index % columnSize];
            Object object =  readObject(buffer,type);
            recordArray[index] = record;
            index++;
        }
//        records.setRecords();
//        records.setTypes();
//        records.setNames();
        return records;
    }

    private static byte[] writeObject(Object data, DataType dataType) {
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
                int arrayLength = ((Object[]) data).length;
                byte[] arrayLengthByte = BytesUtils.intToBytes(arrayLength);
                byte[][] arrayData = new byte[arrayLength][];
                for (int i = 0; i < ((Object[]) data).length; i++) {
                    Object dataEle = ((Object[]) data)[i];
                    DataType eleType = DataType.getType(dataEle.getClass());
                    byte[] typeByte = eleType.serialize();
                    byte[] eleBytes = writeObject(dataEle, eleType);
                    byte[] eleLength = BytesUtils.intToBytes(eleBytes.length);
                    arrayData[i] = new byte[4 + typeByte.length + eleBytes.length];
                    System.arraycopy(eleLength, 0, arrayData[i], 0, 4);
                    System.arraycopy(typeByte, 0, arrayData[i], 4, typeByte.length);
                    System.arraycopy(eleBytes, 0, arrayData[i], 4 + typeByte.length, eleBytes.length);
                }
                int dataSize = Arrays.stream(arrayData).mapToInt(ele -> ele.length).sum();
                byte[] arrayDataByte = new byte[4 + dataSize];
                System.arraycopy(arrayLengthByte, 0, arrayDataByte, 0, 4);
                int pos = 4;
                for (byte[] arrayDatum : arrayData) {
                    System.arraycopy(arrayDatum, 0, arrayDataByte, pos, arrayDatum.length);
                    pos += arrayDatum.length;
                }
                return arrayDataByte;
            default:
                throw new UnSupportedDataTypeException(String.format("%s is not support yet.", dataType));
        }
    }


    private static Object readObject(ByteBuffer buffer, DataType dataType) {
        switch (dataType) {
            case BOOLEAN:
                return BytesUtils.bytesToBool(buffer.get());
            case SMALLINT:
                return buffer.getShort();
            case INTEGER:
                return buffer.getInt();
            case BIGINT:
                return buffer.getLong();
            case FLOAT:
                return buffer.getFloat();
            case DOUBLE:
                return buffer.getDouble();
            case DATE:
                return new Date(buffer.getLong());
            case TIMESTAMP:
                return new Timestamp(buffer.getLong());
            case VARCHAR:
                int size = buffer.getInt();
                byte[] data = new byte[size];
                buffer.get(data);
                return new String(data);
            case BINARY:
                byte[] binary = new byte[buffer.getInt()];
                buffer.get(binary);
                return new Binary(binary);
            case ARRAY:
                int length = buffer.getInt();
                Object[] objects = new Object[length];
                for (int i = 0; i < length; i++) {
                    DataType eleType = DataType.deserialize(buffer.getInt());
                    objects[i] = readObject(buffer,eleType);
                }
            default:
                throw new UnSupportedDataTypeException(String.format("%s is not support yet.", dataType));
        }
    }
}
