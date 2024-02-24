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

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ReadUtils {


    public static int readUnsignedVarInt(InputStream in) throws IOException {
        int value = 0;
        int i = 0;
        int b = in.read();
        while (b != -1 && (b & 0x80) != 0) {
            value |= (b & 0x7F) << i;
            i += 7;
            b = in.read();
        }
        return value | (b << i);
    }

    public static int readVarInt(InputStream in) throws IOException {
        int value = readUnsignedVarInt(in);
        int x = value >>> 1;
        if ((value & 1) != 0) {
            x = ~x;
        }
        return x;
    }

    public static int readUnsignedVarInt(ByteBuffer buffer) {
        int value = 0;
        int i = 0;
        int b = 0;
        while (buffer.hasRemaining() && ((b = buffer.get()) & 0x80) != 0) {
            value |= (b & 0x7F) << i;
            i += 7;
        }
        return value | (b << i);
    }

    public static int readVarInt(ByteBuffer buffer) {
        int value = readUnsignedVarInt(buffer);
        int x = value >>> 1;
        if ((value & 1) != 0) {
            x = ~x;
        }
        return x;
    }

}
