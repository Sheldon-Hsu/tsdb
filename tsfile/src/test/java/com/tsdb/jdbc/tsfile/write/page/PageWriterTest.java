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

package com.tsdb.jdbc.tsfile.write.page;

import com.tsdb.jdbc.common.data.DataType;
import com.tsdb.jdbc.tsfile.encoding.TSEncoding;
import com.tsdb.jdbc.tsfile.memory.AlignedTVList;
import com.tsdb.jdbc.tsfile.memory.TVList;
import com.tsdb.jdbc.tsfile.meta.Table;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsdb.jdbc.common.data.DataType.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PageWriterTest {
    int dataSize = 100;
    int columnSize = 5;
    DataType[] dataType = new DataType[]{INTEGER, BIGINT, FLOAT, DOUBLE, VARCHAR};
    long startTime = 10000;
    TVList tvList;
    Table table;
    PageWriter pageWriter;
    long[] timestamps = new long[dataSize];
    List<Object[]> lineValue = new ArrayList<>();

    @BeforeAll
    void init() {
        table = new Table();
        table.setDataTypes(dataType);
        Map<DataType, TSEncoding> encoder = new HashMap<>();
        for (DataType type : dataType) {
            encoder.put(type, TSEncoding.PLAIN);
        }
        table.setEncoder(encoder);
        pageWriter = new PageWriter(table);
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