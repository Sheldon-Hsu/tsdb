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
