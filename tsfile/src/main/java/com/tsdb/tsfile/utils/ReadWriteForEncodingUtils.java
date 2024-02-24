package com.tsdb.tsfile.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Utils to read/write stream.
 */
public class ReadWriteForEncodingUtils {
    private static final String TOO_LONG_BYTE_FORMAT =
            "tsfile-common BytesUtils: encountered value (%d) that requires more than 4 bytes";

    private ReadWriteForEncodingUtils() {
    }


    public static int getIntMaxBitWidth(List<Integer> list) {
        int max = 1;
        for (int num : list) {
            int bitWidth = 32 - Integer.numberOfLeadingZeros(num);
            max = Math.max(bitWidth, max);
        }
        return max;
    }

    public static int getLongMaxBitWidth(List<Long> list) {
        int max = 1;
        for (long num : list) {
            int bitWidth = 64 - Long.numberOfLeadingZeros(num);
            max = Math.max(bitWidth, max);
        }
        return max;
    }


    public static void writeLongLittleEndianPaddedOnBitWidth(
            long value, OutputStream out, int bitWidth) throws IOException {
        int paddedByteNum = (bitWidth + 7) / 8;
        if (paddedByteNum > 8) {
            throw new IOException(String.format(TOO_LONG_BYTE_FORMAT, paddedByteNum));
        }
        out.write(BytesUtils.longToBytes(value, paddedByteNum));
    }


    /**
     * write a value to stream using unsigned var int format. for example, int 123456789 has its
     * binary format 00000111-01011011-11001101-00010101 (if we omit the first 5 0, then it is
     * 111010-1101111-0011010-0010101), function writeUnsignedVarInt will split every seven bits and
     * write them to stream from low bit to high bit like: 1-0010101 1-0011010 1-1101111 0-0111010 1
     * represents has next byte to write, 0 represents number end.
     *
     * @param value value to write into stream
     * @param out   output stream
     * @return the number of bytes that the value consume.
     */
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


    /**
     * write integer value using special bit to output stream.
     *
     * @param value    value to write to stream
     * @param out      output stream
     * @param bitWidth bit length
     * @throws IOException exception in IO
     */
    public static void writeIntLittleEndianPaddedOnBitWidth(int value, OutputStream out, int bitWidth)
            throws IOException {
        int paddedByteNum = (bitWidth + 7) / 8;
        if (paddedByteNum > 4) {
            throw new IOException(String.format(TOO_LONG_BYTE_FORMAT, paddedByteNum));
        }
        int offset = 0;
        while (paddedByteNum > 0) {
            out.write((value >>> offset) & 0xFF);
            offset += 8;
            paddedByteNum--;
        }
    }


}
