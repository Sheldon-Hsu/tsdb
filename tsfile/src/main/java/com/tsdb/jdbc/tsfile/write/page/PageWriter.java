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

package com.tsdb.jdbc.tsfile.write.page;

import com.tsdb.jdbc.common.data.Binary;
import com.tsdb.jdbc.common.data.DataType;
import com.tsdb.jdbc.common.io.PublicBAOS;
import com.tsdb.jdbc.tsfile.compress.ICompressor;
import com.tsdb.jdbc.tsfile.encoding.EncodingBuilder;
import com.tsdb.jdbc.tsfile.encoding.TSEncoding;
import com.tsdb.jdbc.tsfile.encoding.encode.Encoder;
import com.tsdb.jdbc.tsfile.exception.write.DataNotMatchException;
import com.tsdb.jdbc.tsfile.memory.TVList;
import com.tsdb.jdbc.tsfile.file.header.statistics.FileStatistics;
import com.tsdb.jdbc.tsfile.file.header.statistics.PageStatistics;
import com.tsdb.jdbc.tsfile.meta.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.tsdb.jdbc.common.data.DataType.BIGINT;

public class PageWriter {
    private static final Logger logger = LoggerFactory.getLogger(PageWriter.class);
    private ICompressor compressor;

    // time
    private Encoder timeEncoder;
    private PublicBAOS timeOut;
    // value
    private Encoder[] valueEncoders;
    private PublicBAOS valueOut;

    private FileStatistics statistics;
    private DataType[] dataTypes;


    public PageWriter(Table table) {
        dataTypes = table.getDataTypesOrdered();
        compressor = ICompressor.getCompressor(table.getCompressionType());
        int columnSize = dataTypes.length;
        Map<DataType, TSEncoding> encoderMap = table.getEncoders();
        valueEncoders = new Encoder[columnSize];
        for (int i = 0; i < columnSize; i++) {
            DataType dataType = dataTypes[i];
            valueEncoders[i] = getEncoderByDataType(dataType, encoderMap);
        }
        timeEncoder = getEncoderByDataType(BIGINT, encoderMap);
        this.timeOut = new PublicBAOS();
        this.valueOut = new PublicBAOS();
        this.statistics = new PageStatistics();
    }


    public void transTVList2Page(TVList tvList) {
        check(tvList);
        List<long[]> times = tvList.getTimestamps();
        List<Object[]> values = tvList.getValues();
        if (tvList.getRowCount() == 0) return;
        int rowCount = tvList.getRowCount();
        int lastArrayElementSize = rowCount % TVList.ARRAY_SIZE;
        for (int i = 0; i < values.size() - 1; i++) {
            writeTimeStamps(times.get(i));
            Object columnValues = values.get(i);
            for (int columnIndex = 0; columnIndex < dataTypes.length; columnIndex++) {
                transAndWrite(columnIndex, dataTypes[columnIndex], columnValues, TVList.ARRAY_SIZE);
            }
        }
        Object[] last = values.get(values.size() - 1);
        long[] lastTimes = times.get(times.size() - 1);
        lastTimes = Arrays.copyOf(lastTimes, lastArrayElementSize);
        writeTimeStamps(lastTimes);
        for (int columnIndex = 0; columnIndex < dataTypes.length; columnIndex++) {
            Object lastArray = last[columnIndex];

            transAndWrite(columnIndex, dataTypes[columnIndex], lastArray, lastArrayElementSize);
        }
    }

    private void transAndWrite(int index, DataType dataType, Object columnValues, int stopIndex) {
        switch (dataType) {
            case BOOLEAN:
                writeColumn(valueEncoders[index], (boolean[]) columnValues, stopIndex);
                break;
            case INTEGER:
                writeColumn(valueEncoders[index], (int[]) columnValues, stopIndex);
                break;
            case FLOAT:
                writeColumn(valueEncoders[index], (float[]) columnValues, stopIndex);
                break;
            case BIGINT:
                writeColumn(valueEncoders[index], (long[]) columnValues, stopIndex);
                break;
            case DOUBLE:
                writeColumn(valueEncoders[index], (double[]) columnValues, stopIndex);
                break;
            case VARCHAR:
                writeColumn(valueEncoders[index], (byte[][]) columnValues, stopIndex);
                break;
            case BINARY:
                writeColumn(valueEncoders[index], (Binary[]) columnValues, stopIndex);
                break;
        }
    }

    /**
     * write a column of data
     **/
    public void writeColumn(Encoder encoder, boolean[] values, int stopIndex) {
        for (int i = 0; i < values.length && i < stopIndex; i++) {
            boolean value = values[i];
            encoder.encode(value, valueOut);
        }
    }

    public void writeColumn(Encoder encoder, short[] values, int stopIndex) {
        for (int i = 0; i < values.length && i < stopIndex; i++) {
            short value = values[i];
            encoder.encode(value, valueOut);
        }
    }

    public void writeColumn(Encoder encoder, int[] values, int stopIndex) {
        for (int i = 0; i < values.length && i < stopIndex; i++) {
            int value = values[i];
            encoder.encode(value, valueOut);
        }
    }

    public void writeColumn(Encoder encoder, float[] values, int stopIndex) {
        for (int i = 0; i < values.length && i < stopIndex; i++) {
            float value = values[i];
            encoder.encode(value, valueOut);
        }
    }

    public void writeColumn(Encoder encoder, long[] values, int stopIndex) {
        for (int i = 0; i < values.length && i < stopIndex; i++) {
            long value = values[i];
            encoder.encode(value, valueOut);
        }
    }

    public void writeColumn(Encoder encoder, double[] values, int stopIndex) {
        for (int i = 0; i < values.length && i < stopIndex; i++) {
            double value = values[i];
            encoder.encode(value, valueOut);
        }
    }

    public void writeColumn(Encoder encoder, byte[][] values, int stopIndex) {
        for (int i = 0; i < values.length && i < stopIndex; i++) {
            byte[] value = values[i];
            encoder.encode(value, valueOut);
        }
    }


    public void writeColumn(Encoder encoder, Binary[] values, int stopIndex) {
        for (int i = 0; i < values.length && i < stopIndex; i++) {
            Binary value = values[i];
            encoder.encode(value, valueOut);
        }
    }


    /**
     * write timestamps
     */
    public void writeTimeStamps(long[] timestamps) {
        for (int i = 0; i < timestamps.length; i++) {
            long timestamp = timestamps[i];
            timeEncoder.encode(timestamp, timeOut);
        }
    }

    private Encoder getEncoderByDataType(DataType dataType, Map<DataType, TSEncoding> encoderMap) {
        return EncodingBuilder.getEncodingBuilder(encoderMap.get(dataType)).getEncoder(dataType);
    }

    private void check(TVList tvList) {
        if (!Arrays.equals(tvList.getColumnDataType(), dataTypes)) {
            throw new DataNotMatchException("data types in pageWriter", "data types in  TVList");
        }
        if (tvList.getValues().size() != tvList.getTimestamps().size()) {
            throw new DataNotMatchException("timestamps in TVList", "values in TVList");
        }

    }
}
