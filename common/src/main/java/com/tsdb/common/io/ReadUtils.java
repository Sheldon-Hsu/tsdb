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
