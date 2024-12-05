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

package com.tsdb.jdbc.common.io;

import com.tsdb.jdbc.common.data.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.tsdb.jdbc.common.config.TSDBConstant.STRING_CHARSET;

public class BytesUtils {
    private static final Logger logger = LoggerFactory.getLogger(BytesUtils.class);

    /**
     * integer convert to byte[4].
     *
     * @param i integer to convert
     * @return byte[4] for integer
     */
    public static byte[] intToBytes(int i) {
        return new byte[]{
                (byte) ((i >> 24) & 0xFF),
                (byte) ((i >> 16) & 0xFF),
                (byte) ((i >> 8) & 0xFF),
                (byte) (i & 0xFF)
        };
    }


    /**
     * integer convert to byte array, then write four bytes to parameter desc start from index:offset.
     *
     * @param i      integer to convert
     * @param desc   byte array be written
     * @param offset position in desc byte array that conversion result should start
     * @return byte array
     */
    public static byte[] intToBytes(int i, byte[] desc, int offset) {
        if (desc.length - offset < 4) {
            throw new IllegalArgumentException("Invalid input: desc.length - offset < 4");
        }
        desc[0 + offset] = (byte) ((i >> 24) & 0xFF);
        desc[1 + offset] = (byte) ((i >> 16) & 0xFF);
        desc[2 + offset] = (byte) ((i >> 8) & 0xFF);
        desc[3 + offset] = (byte) (i & 0xFF);
        return desc;
    }

    /**
     * byte[4] convert to integer.
     *
     * @param bytes input byte[]
     * @return integer
     */
    public static int bytesToInt(byte[] bytes) {
        // compatible to long

        int length = bytes.length;
        long r = 0;
        for (int i = 0; i < length; i++) {
            r += ((bytes[length - 1 - i] & 0xFF) << (i * 8));
        }

        if (r > Integer.MAX_VALUE) {
            throw new RuntimeException("Row count is larger than Integer.MAX_VALUE");
        }

        return (int) r;
    }


    /**
     * convert four-bytes byte array cut from parameters to integer.
     *
     * @param bytes  source bytes which length should be greater than 4
     * @param offset position in parameter byte array that conversion result should start
     * @return integer
     */
    public static int bytesToInt(byte[] bytes, int offset) {
        if (bytes.length - offset < 4) {
            throw new IllegalArgumentException("Invalid input: bytes.length - offset < 4");
        }

        int value = 0;
        // high bit to low
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (bytes[offset + i] & 0x000000FF) << shift;
        }
        return value;
    }


    /**
     * convert float to byte array.
     *
     * @param x float
     * @return byte[4]
     */
    public static byte[] floatToBytes(float x) {
        byte[] b = new byte[4];
        int l = Float.floatToIntBits(x);
        for (int i = 3; i >= 0; i--) {
            b[i] = (byte) l;
            l = l >> 8;
        }
        return b;
    }

    /**
     * float convert to boolean, then write four bytes to parameter desc start from index:offset.
     *
     * @param x      float
     * @param desc   byte array be written
     * @param offset position in desc byte array that conversion result should start
     */
    public static void floatToBytes(float x, byte[] desc, int offset) {
        if (desc.length - offset < 4) {
            throw new IllegalArgumentException("Invalid input: desc.length - offset < 4");
        }
        int l = Float.floatToIntBits(x);
        for (int i = 3 + offset; i >= offset; i--) {
            desc[i] = (byte) l;
            l = l >> 8;
        }
    }

    /**
     * convert byte[4] to float.
     *
     * @param b byte[4]
     * @return float
     */
    public static float bytesToFloat(byte[] b) {
        if (b.length != 4) {
            throw new IllegalArgumentException("Invalid input: b.length != 4");
        }

        int l;
        l = b[3];
        l &= 0xff;
        l |= ((long) b[2] << 8);
        l &= 0xffff;
        l |= ((long) b[1] << 16);
        l &= 0xffffff;
        l |= ((long) b[0] << 24);
        return Float.intBitsToFloat(l);
    }

    /**
     * convert four-bytes byte array cut from parameters to float.
     *
     * @param b      source bytes which length should be greater than 4
     * @param offset position in parameter byte array that conversion result should start
     * @return float
     */
    public static float bytesToFloat(byte[] b, int offset) {
        if (b.length - offset < 4) {
            throw new IllegalArgumentException("Invalid input: b.length - offset < 4");
        }

        int l;
        l = b[offset + 3];
        l &= 0xff;
        l |= ((long) b[offset + 2] << 8);
        l &= 0xffff;
        l |= ((long) b[offset + 1] << 16);
        l &= 0xffffff;
        l |= ((long) b[offset] << 24);
        return Float.intBitsToFloat(l);
    }

    /**
     * convert double to byte array.
     *
     * @param data double
     * @return byte[8]
     */
    public static byte[] doubleToBytes(double data) {
        byte[] bytes = new byte[8];
        long value = Double.doubleToLongBits(data);
        for (int i = 7; i >= 0; i--) {
            bytes[i] = (byte) value;
            value = value >> 8;
        }
        return bytes;
    }

    /**
     * convert double to byte into the given byte array started from offset.
     *
     * @param d      input double
     * @param bytes  target byte[]
     * @param offset start pos
     */
    public static void doubleToBytes(double d, byte[] bytes, int offset) {
        if (bytes.length - offset < 8) {
            throw new IllegalArgumentException("Invalid input: bytes.length - offset < 8");
        }

        long value = Double.doubleToLongBits(d);
        for (int i = 7; i >= 0; i--) {
            bytes[offset + i] = (byte) value;
            value = value >> 8;
        }
    }

    /**
     * convert byte array to double.
     *
     * @param bytes byte[8]
     * @return double
     */
    public static double bytesToDouble(byte[] bytes) {
        long value = bytes[7];
        value &= 0xff;
        value |= ((long) bytes[6] << 8);
        value &= 0xffff;
        value |= ((long) bytes[5] << 16);
        value &= 0xffffff;
        value |= ((long) bytes[4] << 24);
        value &= 0xffffffffL;
        value |= ((long) bytes[3] << 32);
        value &= 0xffffffffffL;
        value |= ((long) bytes[2] << 40);
        value &= 0xffffffffffffL;
        value |= ((long) bytes[1] << 48);
        value &= 0xffffffffffffffL;
        value |= ((long) bytes[0] << 56);
        return Double.longBitsToDouble(value);
    }

    /**
     * convert eight-bytes byte array cut from parameters to double.
     *
     * @param bytes  source bytes which length should be greater than 8
     * @param offset position in parameter byte array that conversion result should start
     * @return double
     */
    public static double bytesToDouble(byte[] bytes, int offset) {
        if (bytes.length - offset < 8) {
            throw new IllegalArgumentException("Invalid input: bytes.length - offset < 8");
        }
        long value = bytes[offset + 7];
        value &= 0xff;
        value |= ((long) bytes[offset + 6] << 8);
        value &= 0xffff;
        value |= ((long) bytes[offset + 5] << 16);
        value &= 0xffffff;
        value |= ((long) bytes[offset + 4] << 24);
        value &= 0xffffffffL;
        value |= ((long) bytes[offset + 3] << 32);
        value &= 0xffffffffffL;
        value |= ((long) bytes[offset + 2] << 40);
        value &= 0xffffffffffffL;
        value |= ((long) bytes[offset + 1] << 48);
        value &= 0xffffffffffffffL;
        value |= ((long) bytes[offset] << 56);
        return Double.longBitsToDouble(value);
    }

    /**
     * convert boolean to byte[1].
     *
     * @param x boolean
     * @return byte[]
     */
    public static byte[] boolToBytes(boolean x) {
        byte[] b = new byte[1];
        if (x) {
            b[0] = 1;
        } else {
            b[0] = 0;
        }
        return b;
    }

    public static byte boolToByte(boolean x) {
        if (x) {
            return 1;
        } else {
            return 0;
        }
    }

    public static boolean byteToBool(byte b) {
        return b == 1;
    }

    /**
     * boolean convert to byte array, then write four bytes to parameter desc start from index:offset.
     *
     * @param x      input boolean
     * @param desc   byte array be written
     * @param offset position in desc byte array that conversion result should start
     * @return byte[1]
     */
    public static byte[] boolToBytes(boolean x, byte[] desc, int offset) {
        if (x) {
            desc[offset] = 1;
        } else {
            desc[offset] = 0;
        }
        return desc;
    }

    /**
     * byte array to boolean.
     *
     * @param b input byte[1]
     * @return boolean
     */
    public static boolean bytesToBool(byte[] b) {
        if (b.length != 1) {
            throw new IllegalArgumentException("Invalid input: b.length != 1");
        }

        return b[0] != 0;
    }

    /**
     * byte array to boolean.
     *
     * @param b input byte[1]
     * @return boolean
     */
    public static boolean bytesToBool(byte b) {

        return b != 0;
    }

    /**
     * convert one-bytes byte array cut from parameters to boolean.
     *
     * @param b      source bytes which length should be greater than 1
     * @param offset position in parameter byte array that conversion result should start
     * @return boolean
     */
    public static boolean bytesToBool(byte[] b, int offset) {
        if (b.length - offset < 1) {
            throw new IllegalArgumentException("Invalid input: b.length - offset < 1");
        }
        return b[offset] != 0;
    }

    /**
     * long to byte array with default converting length 8. It means the length of result byte array
     * is 8.
     *
     * @param num long variable to be converted
     * @return byte[8]
     */
    public static byte[] longToBytes(long num) {
        return longToBytes(num, 8);
    }

    /**
     * specify the result array length. then, convert long to Big-Endian byte from low to high. <br>
     * e.g.<br>
     * the binary presentation of long number 1000L is {6 bytes equal 0000000} 00000011 11101000<br>
     * if len = 2, it will return byte array :{00000011 11101000}(Big-Endian) if len = 1, it will
     * return byte array :{11101000}.
     *
     * @param num long variable to be converted
     * @param len length of result byte array
     * @return byte array which length equals with parameter len
     */
    public static byte[] longToBytes(long num, int len) {
        byte[] byteNum = new byte[len];
        for (int ix = 0; ix < len; ix++) {
            byteNum[len - ix - 1] = (byte) ((num >> ix * 8) & 0xFF);
        }
        return byteNum;
    }

    /**
     * long convert to byte array, then write four bytes to parameter desc start from index:offset.
     *
     * @param num    input long variable
     * @param desc   byte array be written
     * @param offset position in desc byte array that conversion result should start
     * @return byte array
     */
    public static byte[] longToBytes(long num, byte[] desc, int offset) {
        for (int ix = 0; ix < 8; ++ix) {
            int i = 64 - (ix + 1) * 8;
            desc[ix + offset] = (byte) ((num >> i) & 0xff);
        }
        return desc;
    }

    /**
     * convert a long to a byte array which length is width, then copy this array to the parameter
     * result from pos.
     *
     * @param srcNum input long variable. All but the lowest {@code width} bits are 0.
     * @param result byte array to convert
     * @param pos    start position
     * @param width  bit-width
     */
    public static void longToBytes(long srcNum, byte[] result, int pos, int width) {
        int cnt = pos & 0x07;
        int index = pos >> 3;
        try {
            while (width > 0) {
                int m = width + cnt >= 8 ? 8 - cnt : width;
                width -= m;
                int mask = 1 << (8 - cnt);
                cnt += m;
                byte y = (byte) (srcNum >>> width);
                y = (byte) (y << (8 - cnt));
                mask = ~(mask - (1 << (8 - cnt)));
                result[index] = (byte) (result[index] & mask | y);
                srcNum = srcNum & ~(-1L << width);
                if (cnt == 8) {
                    index++;
                    cnt = 0;
                }
            }
        } catch (Exception e) {
            logger.error(
                    "tsfile-common BytesUtils: cannot convert a long {} to a byte array, "
                            + "pos {}, width {}",
                    srcNum,
                    pos,
                    width,
                    e);
        }
    }

    /**
     * convert byte array to long with default length 8. namely.
     *
     * @param byteNum input byte array
     * @return long
     */
    public static long bytesToLong(byte[] byteNum) {
        return bytesToLong(byteNum, byteNum.length);
    }

    /**
     * specify the input byte array length. then, convert byte array to long value from low to high.
     * <br>
     * e.g.<br>
     * the input byte array is {00000011 11101000}. if len = 2, return 1000 if len = 1, return
     * 232(only calculate the low byte).
     *
     * @param byteNum byte array to be converted
     * @param len     length of input byte array to be converted
     * @return long
     */
    public static long bytesToLong(byte[] byteNum, int len) {
        long num = 0;
        for (int ix = 0; ix < len; ix++) {
            num <<= 8;
            num |= (byteNum[ix] & 0xff);
        }
        return num;
    }

    /**
     * given a byte array, read width bits from specified pos bits and convert it to an long.
     *
     * @param result input byte array
     * @param pos    bit offset rather than byte offset
     * @param width  bit-width
     * @return long variable
     */
    public static long bytesToLong(byte[] result, int pos, int width) {
        long ret = 0;
        int cnt = pos & 0x07;
        int index = pos >> 3;
        while (width > 0) {
            int m = width + cnt >= 8 ? 8 - cnt : width;
            width -= m;
            ret = ret << m;
            byte y = (byte) (result[index] & (0xff >> cnt));
            y = (byte) ((y & 0xff) >>> (8 - cnt - m));
            ret = ret | (y & 0xff);
            cnt += m;
            if (cnt == 8) {
                cnt = 0;
                index++;
            }
        }
        return ret;
    }

    /**
     * convert eight-bytes byte array cut from parameters to long.
     *
     * @param byteNum source bytes which length should be greater than 8
     * @param len     length of input byte array to be converted
     * @param offset  position in parameter byte array that conversion result should start
     * @return long
     */
    public static long bytesToLongFromOffset(byte[] byteNum, int len, int offset) {
        if (byteNum.length - offset < len) {
            throw new IllegalArgumentException("Invalid input: byteNum.length - offset < len");
        }
        long num = 0;
        for (int ix = 0; ix < len; ix++) {
            num <<= 8;
            num |= (byteNum[offset + ix] & 0xff);
        }
        return num;
    }

    /**
     * convert string to byte array using UTF-8 encoding.
     *
     * @param str input string
     * @return byte array
     */
    public static byte[] stringToBytes(String str) {
        return str.getBytes(STRING_CHARSET);
    }

    /**
     * convert byte array to string using UTF-8 encoding.
     *
     * @param byteStr input byte array
     * @return string
     */
    public static String bytesToString(byte[] byteStr) {
        return new String(byteStr, STRING_CHARSET);
    }


    /**
     * we modify the order of serialization for fitting ByteBuffer.getShort()
     *
     * @param b bytes
     * @return short number
     */
    public static short bytesToShort(byte[] b) {
        short s0 = (short) (b[1] & 0xff);
        short s1 = (short) (b[0] & 0xff);
        s1 <<= 8;
        short s = (short) (s0 | s1);
        return s;
    }


    /**
     * we modify the order of serialization for fitting ByteBuffer.putShort()
     *
     * @param number input short number
     * @return Bytes
     */
    public static byte[] shortToBytes(short number) {
        int temp = number;
        byte[] b = new byte[2];
        for (int i = b.length - 1; i >= 0; i--) {
            b[i] = (byte) temp;
            temp = temp >> 8;
        }

        return b;
    }


    public static Binary valueOf(String value) {
        return new Binary(stringToBytes(value));
    }
}
