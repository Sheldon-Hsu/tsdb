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

package com.tsdb.tsfile.file.header.statistics.value;

import com.tsdb.common.data.DataType;
import com.tsdb.common.io.ReadUtils;
import com.tsdb.tsfile.exception.write.UnsupportedColumnTypeException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

public abstract class ValueStatistics<T> {

    protected int count = 0;
    protected DataType dataType;

    protected double doubleSum;
    protected long longSum;


    static final String STATS_UNSUPPORTED_MSG = "%s statistics does not support: %s";

    public abstract void deserialize(InputStream inputStream) throws IOException;

    public abstract void deserialize(ByteBuffer byteBuffer);


    public static ValueStatistics<?> deserialize(InputStream inputStream, DataType dataType) throws IOException {
        ValueStatistics<? extends Serializable> statistics = getStatsByType(dataType);
        statistics.setCount(ReadUtils.readUnsignedVarInt(inputStream));
        statistics.deserialize(inputStream);

        return statistics;
    }

    public static ValueStatistics<?> deserialize(ByteBuffer buffer, DataType dataType) {
        ValueStatistics<? extends Serializable> statistics = getStatsByType(dataType);
        statistics.setCount(ReadUtils.readUnsignedVarInt(buffer));

        statistics.deserialize(buffer);
        return statistics;
    }

    public static ValueStatistics<? extends Serializable> getStatsByType(DataType type) {
        switch (type) {
            case INTEGER:
                return new IntegerStatistics();
            case BIGINT:
                return new LongStatistics();
            case VARCHAR:
                return new StringStatistics();
            case BOOLEAN:
                return new BooleanStatistics();
            case DOUBLE:
                return new DoubleStatistics();
            case FLOAT:
                return new FloatStatistics();
            default:
                throw new UnsupportedColumnTypeException(type.toString());
        }
    }


    public abstract T getMinValue();

    public abstract T getMaxValue();

    public abstract T getFirstValue();

    public abstract T getLastValue();

    public abstract double getSumDoubleValue();

    public abstract long getSumLongValue();

    public abstract void update(T value);


    public void setCount(int count) {
        this.count = count;
    }

    public DataType getDataType() {
        return dataType;
    }
}
