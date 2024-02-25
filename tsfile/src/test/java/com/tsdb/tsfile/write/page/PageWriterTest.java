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

import com.tsdb.common.data.DataType;
import com.tsdb.tsfile.encoding.TSEncoding;
import com.tsdb.tsfile.memory.AlignedTVList;
import com.tsdb.tsfile.memory.TVList;
import com.tsdb.tsfile.meta.Schema;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsdb.common.data.DataType.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PageWriterTest {
    int dataSize = 100;
    int columnSize = 5;
    DataType[] dataType = new DataType[]{INTEGER, BIGINT, FLOAT, DOUBLE, VARCHAR};
    long startTime = 10000;
    TVList tvList;
    Schema schema;
    PageWriter pageWriter;
    long[] timestamps = new long[dataSize];
    List<Object[]> lineValue = new ArrayList<>();

    @BeforeAll
    void init() {
        schema = new Schema();
        schema.setDataTypes(dataType);
        Map<DataType, TSEncoding> encoder = new HashMap<>();
        for (DataType type : dataType) {
            encoder.put(type, TSEncoding.PLAIN);
        }
        schema.setEncoder(encoder);
        pageWriter = new PageWriter(schema);
        tvList = new AlignedTVList(dataType);
        for (int lineIndex = 0; lineIndex < dataSize; lineIndex++) {
            timestamps[lineIndex] = startTime + lineIndex + 1;
            Object[] line = new Object[columnSize];
            line[0] = 1;
            line[1] = 1L;
            line[2] = 1f;
            line[3] = 1d;
            line[4] = "column-" + lineIndex;
            lineValue.add(line);
        }
        int dataSize = timestamps.length;
        for (int i = 0; i < dataSize; i++) {
            tvList.put(timestamps[i], lineValue.get(i));
        }
    }

    @Test
    void transTVList2Page() {
        pageWriter.transTVList2Page(tvList);
    }

    @Test
    void writeColumn() {
    }

    @Test
    void writeTimeStamps() {
    }
}