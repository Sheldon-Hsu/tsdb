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

package com.tsdb.tsfile.encoding;

public enum TSEncoding {
    PLAIN((byte) 0),

    RLE((byte) 2),
    DIFF((byte) 3),
    TS_2DIFF((byte) 4),
    BITMAP((byte) 5),
    REGULAR((byte) 7),
    GORILLA((byte) 8),
    ZIGZAG((byte) 9),

    CHIMP((byte) 11),
    SPRINTZ((byte) 12),
    RLBE((byte) 13);
    private final byte type;

    TSEncoding(byte type) {
        this.type = type;
    }

    /**
     * deserialize encoding deserialize type.
     *
     * @param encoding -use to determine encoding type
     * @return -encoding type
     */
    public static TSEncoding deserialize(byte encoding) {
        return getTsEncoding(encoding);
    }

    /**
     * serialize encoding type.
     *
     * @return -encoding type
     */
    public byte serialize() {
        return type;
    }

    private static TSEncoding getTsEncoding(byte encoding) {
        switch (encoding) {
            case 0:
                return TSEncoding.PLAIN;
            default:
                throw new IllegalArgumentException("unsupported encoding type: " + encoding);
        }
    }

    public static int getSerializedSize() {
        return Byte.BYTES;
    }


}
