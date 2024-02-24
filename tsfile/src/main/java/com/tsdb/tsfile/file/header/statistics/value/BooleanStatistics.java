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
import com.tsdb.tsfile.exception.filter.StatisticsClassException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class BooleanStatistics extends ValueStatistics<Boolean> {


    private boolean isEmpty = true;
    private boolean min;
    private boolean max;
    /*
        value may be null
     */
    private Boolean first;
    private Boolean last;
    private int count = 0;
    private int noNullCount = 0;

    public BooleanStatistics() {
        dataType = DataType.BOOLEAN;
    }

    @Override
    public void deserialize(InputStream inputStream) throws IOException {

    }

    @Override
    public void deserialize(ByteBuffer byteBuffer) {

    }

    @Override
    public Boolean getMinValue() {
        throw new StatisticsClassException(
                String.format(STATS_UNSUPPORTED_MSG, DataType.BOOLEAN, "min"));
    }

    @Override
    public Boolean getMaxValue() {
        throw new StatisticsClassException(
                String.format(STATS_UNSUPPORTED_MSG, DataType.BOOLEAN, "max"));
    }

    @Override
    public Boolean getFirstValue() {
        return first;
    }

    @Override
    public Boolean getLastValue() {
        return last;
    }

    @Override
    public double getSumDoubleValue() {
        throw new StatisticsClassException(
                String.format(STATS_UNSUPPORTED_MSG, DataType.BOOLEAN, "double sum"));
    }

    @Override
    public long getSumLongValue() {
        return 0;
    }

    @Override
    public void update(Boolean value) {
        if (value != null) {
            noNullCount++;
            if (isEmpty) {
                first = value;
                isEmpty = false;
            }
        } else {
            if (isEmpty) {
                first = value;
                isEmpty = false;
            }
        }
        count++;
        last = value;
        longSum += value ? 1 : 0;
    }
}
