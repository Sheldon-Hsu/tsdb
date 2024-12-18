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

package com.tsdb.jdbc.tsfile.encoding.encode.sprintz;

import com.tsdb.jdbc.tsfile.conf.TSFileConfig;
import com.tsdb.jdbc.tsfile.conf.TSFileDescriptor;
import com.tsdb.jdbc.tsfile.encoding.TSEncoding;
import com.tsdb.jdbc.tsfile.encoding.encode.Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class SprintzEncoder extends Encoder {
    protected static final Logger logger = LoggerFactory.getLogger(SprintzEncoder.class);

    // Segment block size to compress:8
    protected int Block_size = 8;

    // group size maximum
    protected int groupMax = 16;

    // group number
    protected int groupNum;

    // the bit width used for bit-packing and rle.
    protected int bitWidth;

    /**
     * output stream to buffer {@code <bitwidth> <encoded-data>}.
     */
    protected ByteArrayOutputStream byteCache;

    // selecet the predict method
    protected String predictMethod =
            TSFileDescriptor.getInstance().getConf().getSprintzPredictScheme();

    protected boolean isFirstCached = false;

    protected TSFileConfig config = TSFileDescriptor.getInstance().getConf();

    protected SprintzEncoder() {
        super(TSEncoding.SPRINTZ);
        byteCache = new ByteArrayOutputStream();
    }

    protected void reset() {
        byteCache.reset();
        isFirstCached = false;
        groupNum = 0;
    }

    protected abstract void bitPack() throws IOException;
}
