package com.tsdb.jdbc.common.utils;

import com.tsdb.jdbc.common.data.Binary;
import com.tsdb.jdbc.common.data.DataType;
import com.tsdb.jdbc.common.data.Record;
import com.tsdb.jdbc.common.data.Records;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Timestamp;

class RecordUtilsTest {

    Records records;

    @BeforeEach
    void setUp() {
        records = new Records();
        String[] names = new String[10];
        names[0] = "tag";
        names[1] = "timestamp";
        names[2] = "short1";
        names[3] = "int2";
        names[4] = "long3";
        names[5] = "float4";
        names[6] = "double5";
        names[7] = "date6";
        names[8] = "binary7";
        names[9] = "array8";
        records.setNames(names);
        DataType[] dataTypes = new DataType[10];
        dataTypes[0] = DataType.VARCHAR;
        dataTypes[1] = DataType.TIMESTAMP;
        dataTypes[2] = DataType.SMALLINT;
        dataTypes[3] = DataType.INTEGER;
        dataTypes[4] = DataType.BIGINT;
        dataTypes[5] = DataType.FLOAT;
        dataTypes[6] = DataType.DOUBLE;
        dataTypes[7] = DataType.DATE;
        dataTypes[8] = DataType.BINARY;
        dataTypes[9] = DataType.ARRAY;
        records.setTypes(dataTypes);
        Record[] recordArray = new Record[10];
        for (int i = 0; i < 10; i++) {
            Record record = new Record();
            Object[] raw = new Object[10];
            raw[0] = "tag";
            raw[1] = new Timestamp(100000L);
            raw[2] = (short)i;
            raw[3] = i;
            raw[4] = i *10000L+1L;
            raw[5] = i *10000F+1F;
            raw[6] = i*10000D+1D;
            raw[7] = new Date(i*100000L+1L);
            raw[8] = new Binary(new byte[]{1,1,1,1,1,1});
            raw[9] = new Object[]{1,"a",1D,1F};
            record.setRaw(raw);
            recordArray[i] = record;
        }
        records.setRecords(recordArray);
    }

    @Test
    void writeRecordsToBytes() {
        Assertions.assertDoesNotThrow(()->  RecordUtils.writeRecordsToBytes(records));

    }

    @Test
    void  readRecords(){
        byte[] bytes =   RecordUtils.writeRecordsToBytes(records);
        Records records =   RecordUtils.readRecordsFromBytes(bytes);
    }
}