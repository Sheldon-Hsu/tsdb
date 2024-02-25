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
    DataType[] dataType = new DataType[]{INTEGER, BIGINT,FLOAT,DOUBLE,VARCHAR};
    long startTime = 10000;
    TVList tvList;
    long[] timestamps = new long[dataSize];
    List<Object[]> lineValue = new ArrayList<>();

    @BeforeAll
    void init() {
        tvList = new AlignedTVList(dataType);
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