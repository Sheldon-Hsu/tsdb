package com.tsdb.tsfile.memory;

import com.tsdb.common.data.DataType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;

import static com.tsdb.common.data.DataType.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TVListTest {
    int dataSize = 100;
    int columnSize = 5;
    DataType[] dataType = new DataType[]{INTEGER,LONG,FLOAT,DOUBLE,VARCHAR};
    long startTime = 10000;
    TVList tvList;
    long[] timestamps = new long[dataSize];
    List<Object[]> lineValue = new ArrayList<>();

    @BeforeAll
    void init() {
        tvList = new TVList(dataType);
        for (int lineIndex = 0; lineIndex < dataSize; lineIndex++) {
            timestamps[lineIndex] = startTime + lineIndex + 1;
            Object[] line = new Object[columnSize];
            for (int columnIndex = 1; columnIndex <= columnSize; columnIndex++) {
                line[columnIndex -1] = "column-" + columnIndex + "-" + lineIndex;
            }
            lineValue.add(line);
        }

    }

    @Test
    void put() {
        int dataSize = timestamps.length;
        for (int i = 0; i < dataSize; i++) {
            tvList.put(timestamps[i], lineValue.get(i));
        }

        Assertions.assertEquals(dataSize, tvList.getRowCount());
        Assertions.assertEquals(startTime + 1, tvList.getMinTime());
        Assertions.assertEquals(startTime + dataSize, tvList.getMaxTime());


    }
}