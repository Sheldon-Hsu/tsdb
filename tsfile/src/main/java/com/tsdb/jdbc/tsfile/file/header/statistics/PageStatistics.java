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

package com.tsdb.jdbc.tsfile.file.header.statistics;

import com.tsdb.jdbc.common.data.Binary;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class PageStatistics extends FileStatistics {

    private Binary spatialSign;


    public PageStatistics() {
    }

    public Binary getSpatialSign() {
        return spatialSign;
    }

    public void setSpatialSign(Binary spatialSign) {
        this.spatialSign = spatialSign;
    }


    @Override
    public int serialize(InputStream inputStream) throws IOException {
        return 0;
    }

    @Override
    public void serialize(ByteBuffer byteBuffer) {

    }

    @Override
    public void deserialize(InputStream inputStream) throws IOException {

    }

    @Override
    public void deserialize(ByteBuffer byteBuffer) {

    }
}
