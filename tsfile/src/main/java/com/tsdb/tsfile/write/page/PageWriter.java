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

package com.tsdb.tsfile.write.page;

import com.tsdb.common.config.TSDBConstant;
import com.tsdb.common.data.Binary;
import com.tsdb.common.data.DataType;
import com.tsdb.common.io.PublicBAOS;
import com.tsdb.tsfile.compress.ICompressor;
import com.tsdb.tsfile.encoding.EncodingBuilder;
import com.tsdb.tsfile.encoding.TSEncoding;
import com.tsdb.tsfile.encoding.encode.Encoder;
import com.tsdb.tsfile.file.header.statistics.FileStatistics;
import com.tsdb.tsfile.file.header.statistics.PageStatistics;
import com.tsdb.tsfile.memory.TVList;
import com.tsdb.tsfile.meta.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.tsdb.common.data.DataType.*;

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


    public PageWriter(Schema schema) {
        dataTypes = schema.getDataTypesOrdered();
        Map<DataType, TSEncoding> encoderMap = schema.getEncoders();
        for (int i = 0; i < dataTypes.length; i++) {
            DataType dataType = dataTypes[i];
            valueEncoders[i] = getEncoderByDataType(dataType, encoderMap);
        }
        timeEncoder = getEncoderByDataType(BIGINT,encoderMap);
        this.timeOut = new PublicBAOS();
        this.valueOut = new PublicBAOS();
        this.statistics = new PageStatistics();
    }




    public void transTVList2Page(TVList tvList) {
        List<long[]> times =  tvList.getTimestamps();
        List<Object[]> values = tvList.getValues();
    }

    /**
     * write a column of data
     **/
    public void writeColumn(int columnIndex, Object[] values) {
//      TODO 这里把一列数据进行编码压缩后 再通过通用的压缩方法进行压缩
        for (int i = 0; i < values.length; i++) {
            DataType dataType = dataTypes[i];
            Object value = values[i];
            switch (dataType) {
                case BOOLEAN:
                    valueEncoders[columnIndex].encode((boolean) value, valueOut);
                case INTEGER:
                    valueEncoders[columnIndex].encode((int) value, valueOut);
                case FLOAT:
                    valueEncoders[columnIndex].encode((float) value, valueOut);
                case BIGINT:
                    valueEncoders[columnIndex].encode((long) value, valueOut);
                case DOUBLE:
                    valueEncoders[columnIndex].encode((double) value, valueOut);
                case VARCHAR:
                    Binary binary = new Binary((String) value, TSDBConstant.STRING_CHARSET);
                    valueEncoders[columnIndex].encode(binary, valueOut);
            }
        }
        statistics.update(columnIndex, values);
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

}
