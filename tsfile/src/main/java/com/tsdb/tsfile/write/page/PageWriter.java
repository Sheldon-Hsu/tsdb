package com.tsdb.tsfile.write.page;

import com.tsdb.common.config.TSDBConstant;
import com.tsdb.common.data.Binary;
import com.tsdb.common.data.DataType;
import com.tsdb.common.io.PublicBAOS;
import com.tsdb.tsfile.compress.ICompressor;
import com.tsdb.tsfile.encoding.encode.Encoder;
import com.tsdb.tsfile.file.header.statistics.FileStatistics;
import com.tsdb.tsfile.file.header.statistics.PageStatistics;
import com.tsdb.tsfile.memory.TVList;
import com.tsdb.tsfile.meta.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageWriter {
    private static final Logger logger = LoggerFactory.getLogger(PageWriter.class);
    private ICompressor compressor;

    // time
    private Encoder timeEncoder;
    private PublicBAOS timeOut;
    // value
    private Encoder valueEncoder;
    private PublicBAOS valueOut;

    private FileStatistics statistics;
    private DataType[] dataTypes;


    public PageWriter(Schema schema) {
        schema.getEncoders();
    }


    private PageWriter(Encoder timeEncoder, Encoder valueEncoder) {
        this.timeOut = new PublicBAOS();
        this.valueOut = new PublicBAOS();
        this.timeEncoder = timeEncoder;
        this.valueEncoder = valueEncoder;
        this.statistics = new PageStatistics();
    }


    public void transTVList2Page(TVList tvList) {
//      todo  这里把tvlist写入到IO流里面
        tvList.getTimestamps();
        tvList.getValues();
    }

    /**
     * write a column of data
     **/
    public void write(int columnIndex, Object[] values) {
//      TODO 这里把一列数据进行编码压缩后 再通过通用的压缩方法进行压缩
        for (int i = 0; i < values.length; i++) {
            DataType dataType = dataTypes[i];
            Object value = values[i];
            switch (dataType) {
                case BOOLEAN:
                    valueEncoder.encode((boolean) value, valueOut);
                case INTEGER:
                    valueEncoder.encode((int) value, valueOut);
                case FLOAT:
                    valueEncoder.encode((float) value, valueOut);
                case BIGINT:
                    valueEncoder.encode((long) value, valueOut);
                case DOUBLE:
                    valueEncoder.encode((double) value, valueOut);
                case VARCHAR:
                    Binary binary = new Binary((String) value, TSDBConstant.STRING_CHARSET);
                    int length = binary.getLength();
                    valueEncoder.encode(length, valueOut);
                    valueEncoder.encode(binary, valueOut);
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


}
