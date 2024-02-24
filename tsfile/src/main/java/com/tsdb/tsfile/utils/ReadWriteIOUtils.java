package com.tsdb.tsfile.utils;


import java.nio.ByteBuffer;


/**
 * ConverterUtils is a utility class. It provides conversion between normal datatype and byte array.
 */
public class ReadWriteIOUtils {


    private static final int NO_BYTE_TO_READ = -1;

    public static int read(ByteBuffer buffer) {
        if (!buffer.hasRemaining()) {
            return NO_BYTE_TO_READ;
        }
        return buffer.get() & 0xFF;
    }
}
