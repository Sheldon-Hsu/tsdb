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

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class FloatStatistics extends ValueStatistics<Float> {

    private boolean isEmpty = true;
    private float min;
    private float max;
    /*
        value may be null
     */
    private Float first;
    private Float last;
    private int count = 0;
    private int noNullCount = 0;

    public FloatStatistics() {
        dataType = DataType.FLOAT;
    }

    @Override
    public void deserialize(InputStream inputStream) throws IOException {

    }

    @Override
    public void deserialize(ByteBuffer byteBuffer) {

    }

    @Override
    public Float getMinValue() {
        return min;
    }

    @Override
    public Float getMaxValue() {
        return max;
    }

    @Override
    public Float getFirstValue() {
        return first;
    }

    @Override
    public Float getLastValue() {
        return last;
    }

    @Override
    public double getSumDoubleValue() {
        return doubleSum;
    }

    @Override
    public long getSumLongValue() {
        return longSum;
    }

    @Override
    public void update(Float value) {
        if (value != null) {
            noNullCount++;
            if (isEmpty) {
                min = value;
                max = value;
                first = value;
                isEmpty = false;
            } else {
                min = Math.min(min, value);
                max = Math.max(max, value);
            }
        } else {
            if (isEmpty) {
                first = value;
                isEmpty = false;
            }
        }
        count++;
        last = value;
        doubleSum += value;
        longSum += value;
    }
}
