package com.tsdb.tsfile.memory;

import com.tsdb.common.data.DataType;
import com.tsdb.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Time series data memory model, for write.
 */
public class TVList {
    private static final Logger LOGGER = LoggerFactory.getLogger(TVList.class);

    public static final int ARRAY_SIZE = 1024;

    /**
     * Arrays can save memory space, but the length cannot be specified when initializing.
     * Using list<long[]> can initialize a fixed-length array,
     * which can be automatically expanded by the list.add method if the array is insufficient.
     * However, list is also dynamically expanded by copying arrays, which can be modified to achieve dynamic expansion of arrays.
     */
    protected List<long[]> timestamps;
    //    todo Try to use basic types instead of boxed type for efficiency
    protected List<Object[][]> values;

    /**
     * Used to calculate the memory size
     */
    private long length;
    private int fixedLength;
    private final Map<Integer, DataType> lengthFloatData = new HashMap<>();
    protected int rowCount;
    protected long maxTime;
    protected long minTime;
    protected int columnSize;
    protected DataType[] columnDataType;

    public TVList(DataType[] columnDataType) {
        this.timestamps = new ArrayList<>();
        this.values = new ArrayList<>();
        this.columnDataType = columnDataType;
        this.columnSize = columnDataType.length;
        this.rowCount = 0;
        calcFixedLength();
    }

    /**
     * Add a record where the data is presented as a row in memory and converted to a column when flushed into the file.
     * Row data structures are easier for developers to understand.
     *
     * @param timestamp
     * @param lineValue
     */
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

    public int getRowCount() {
        return rowCount;
    }

    public long getDataLength() {
        return length;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public long getMinTime() {
        return minTime;
    }

    /**
     * Calculate the fixed length of a row. The fixed length does not change with the row data.
     */
    private void calcFixedLength() {
        for (int i = 0; i < columnDataType.length; i++) {
            DataType dataType = columnDataType[i];
            int columnFixLength = dataType.getFixLength();
            if (columnFixLength > 0) {
                fixedLength += columnFixLength;
            } else {
                lengthFloatData.put(i, dataType);
            }
        }
    }
}
