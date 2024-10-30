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

package com.tsdb.jdbc.tsfile.conf;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TSFileConfig implements Serializable {
    private static final long serialVersionUID = -1582702960721656705L;


    public static final int BYTE_SIZE_PER_CHAR = 4;


    public static final int RLE_MIN_REPEATED_NUM = 8;

    public static final int RLE_MAX_REPEATED_NUM = 0x7FFF;
    public static final int RLE_MAX_BIT_PACKED_NUM = 63;

    public static final int VALUE_BITS_LENGTH_32BIT = 32;
    public static final int LEADING_ZERO_BITS_LENGTH_32BIT = 5;
    public static final int MEANINGFUL_XOR_BITS_LENGTH_32BIT = 5;

    public static final int VALUE_BITS_LENGTH_64BIT = 64;
    public static final int LEADING_ZERO_BITS_LENGTH_64BIT = 6;
    public static final int MEANINGFUL_XOR_BITS_LENGTH_64BIT = 6;

    public static final int GORILLA_ENCODING_ENDING_INTEGER = Integer.MIN_VALUE;
    public static final long GORILLA_ENCODING_ENDING_LONG = Long.MIN_VALUE;
    public static final float GORILLA_ENCODING_ENDING_FLOAT = Float.NaN;
    public static final double GORILLA_ENCODING_ENDING_DOUBLE = Double.NaN;


    /**
     * Global unified configuration
     */
    public static final Charset STRING_CHARSET = StandardCharsets.UTF_8;


    public static final String MAGIC_STRING = "TsFile";


    private int maxStringLength = 128;

    public int getMaxStringLength() {
        return maxStringLength;
    }

    public void setMaxStringLength(int maxStringLength) {
        this.maxStringLength = maxStringLength;
    }


    public String getSprintzPredictScheme() {
        return "fire";
    }
}
