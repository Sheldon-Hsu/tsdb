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

package com.tsdb.common.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class WriteUtils {

    public static int writeUnsignedVarInt(int value, ByteArrayOutputStream out) {
        int position = 1;
        while ((value & 0xFFFFFF80) != 0L) {
            out.write((value & 0x7F) | 0x80);
            value >>>= 7;
            position++;
        }
        out.write(value & 0x7F);
        return position;
    }

    public static int writeVarInt(int value, ByteArrayOutputStream out) {
        int uValue = value << 1;
        if (value < 0) {
            uValue = ~uValue;
        }
        return writeUnsignedVarInt(uValue, out);
    }

    public static int writeUnsignedVarInt(int value, OutputStream out) throws IOException {
        int position = 1;
        while ((value & 0xFFFFFF80) != 0L) {
            out.write((value & 0x7F) | 0x80);
            value >>>= 7;
            position++;
        }
        out.write(value & 0x7F);
        return position;
    }

    public static int writeVarInt(int value, OutputStream out) throws IOException {
        int uValue = value << 1;
        if (value < 0) {
            uValue = ~uValue;
        }
        return writeUnsignedVarInt(uValue, out);
    }

}
