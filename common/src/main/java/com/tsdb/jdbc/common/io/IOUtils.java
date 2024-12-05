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
import com.tsdb.jdbc.common.data.DataType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import static com.tsdb.jdbc.common.config.TSDBConstant.STRING_CHARSET;

public class IOUtils {

    public static final int BOOLEAN_LEN = 1;
    public static final int SHORT_LEN = 2;
    public static final int INT_LEN = 4;
    public static final int LONG_LEN = 8;
    public static final int DOUBLE_LEN = 8;
    public static final int FLOAT_LEN = 4;
    public static final float BIT_LEN = 0.125F;

    private static final int NO_BYTE_TO_READ = -1;


    private static final String RETURN_ERROR = "Intend to read %d bytes but %d are actually returned";


    private IOUtils() {}

    /** read a bool from inputStream. */
    public static boolean readBool(InputStream inputStream) throws IOException {
        int flag = inputStream.read();
        return flag == 1;
    }

    // used by generated code
    @SuppressWarnings("unused")
    public static boolean readBoolean(InputStream inputStream) throws IOException {
        int flag = inputStream.read();
        return flag == 1;
    }

    /** read a bool from byteBuffer. */
    public static boolean readBool(ByteBuffer buffer) {
        byte a = buffer.get();
        return a == 1;
    }

    /** read a Boolean from byteBuffer. */
    public static Boolean readBoolObject(ByteBuffer buffer) {
        byte a = buffer.get();
        if (a == 1) {
            return true;
        } else if (a == 0) {
            return false;
        }
        return null;
    }

    /** read a Boolean from byteBuffer. */
    public static Boolean readBoolObject(InputStream inputStream) throws IOException {
        int flag = inputStream.read();
        if (flag == 1) {
            return true;
        } else if (flag == 0) {
            return false;
        }
        return null;
    }

    /** read a byte from byteBuffer. */
    public static byte readByte(ByteBuffer buffer) {
        return buffer.get();
    }

    /**
     * read bytes array in given size
     *
     * @param buffer buffer
     * @param size size
     * @return bytes array
     */
    public static byte[] readBytes(ByteBuffer buffer, int size) {
        byte[] res = new byte[size];
        buffer.get(res);
        return res;
    }


    /** read a bool from byteBuffer. */
    public static boolean readIsNull(InputStream inputStream) throws IOException {
        return readBool(inputStream);
    }

    /** read a bool from byteBuffer. */
    public static boolean readIsNull(ByteBuffer buffer) {
        return readBool(buffer);
    }

    /** read a byte var from inputStream. */
    public static byte readByte(InputStream inputStream) throws IOException {
        return (byte) inputStream.read();
    }

    /** read a short var from inputStream. */
    public static short readShort(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[SHORT_LEN];
        int readLen = inputStream.read(bytes);
        if (readLen != SHORT_LEN) {
            throw new IOException(String.format(RETURN_ERROR, SHORT_LEN, readLen));
        }
        return BytesUtils.bytesToShort(bytes);
    }

    /** read a short var from byteBuffer. */
    public static short readShort(ByteBuffer buffer) {
        return buffer.getShort();
    }

    /** read a float var from inputStream. */
    public static float readFloat(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[FLOAT_LEN];
        int readLen = inputStream.read(bytes);
        if (readLen != FLOAT_LEN) {
            throw new IOException(String.format(RETURN_ERROR, FLOAT_LEN, readLen));
        }
        return BytesUtils.bytesToFloat(bytes);
    }

    /** read a float var from byteBuffer. */
    public static float readFloat(ByteBuffer byteBuffer) {
        byte[] bytes = new byte[FLOAT_LEN];
        byteBuffer.get(bytes);
        return BytesUtils.bytesToFloat(bytes);
    }

    /** read a double var from inputStream. */
    public static double readDouble(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[DOUBLE_LEN];
        int readLen = inputStream.read(bytes);
        if (readLen != DOUBLE_LEN) {
            throw new IOException(String.format(RETURN_ERROR, DOUBLE_LEN, readLen));
        }
        return BytesUtils.bytesToDouble(bytes);
    }

    /** read a double var from byteBuffer. */
    public static double readDouble(ByteBuffer byteBuffer) {
        byte[] bytes = new byte[DOUBLE_LEN];
        byteBuffer.get(bytes);
        return BytesUtils.bytesToDouble(bytes);
    }

    /** read a int var from inputStream. */
    public static int readInt(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[INT_LEN];
        int readLen = inputStream.read(bytes);
        if (readLen != INT_LEN) {
            throw new IOException(String.format(RETURN_ERROR, INT_LEN, readLen));
        }
        return BytesUtils.bytesToInt(bytes);
    }

    /** read a int var from byteBuffer. */
    public static int readInt(ByteBuffer buffer) {
        return buffer.getInt();
    }

    /**
     * read an unsigned byte(0 ~ 255) as InputStream does.
     *
     * @return the byte or -1(means there is no byte to read)
     */
    public static int read(ByteBuffer buffer) {
        if (!buffer.hasRemaining()) {
            return NO_BYTE_TO_READ;
        }
        return buffer.get() & 0xFF;
    }

    /** read a long var from inputStream. */
    public static long readLong(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[LONG_LEN];
        int readLen = inputStream.read(bytes);
        if (readLen != LONG_LEN) {
            throw new IOException(String.format(RETURN_ERROR, LONG_LEN, readLen));
        }
        return BytesUtils.bytesToLong(bytes);
    }

    /** read a long var from byteBuffer. */
    public static long readLong(ByteBuffer buffer) {
        return buffer.getLong();
    }

    /** read string from inputStream. */
    public static String readString(InputStream inputStream) throws IOException {
        int strLength = readInt(inputStream);
        if (strLength <= 0) {
            return null;
        }
        byte[] bytes = new byte[strLength];
        int readLen = inputStream.read(bytes, 0, strLength);
        if (readLen != strLength) {
            throw new IOException(String.format(RETURN_ERROR, strLength, readLen));
        }
        return new String(bytes, 0, strLength);
    }

    /** string length's type is varInt */
    public static String readVarIntString(InputStream inputStream) throws IOException {
        int strLength = ReadWriteForEncodingUtils.readVarInt(inputStream);
        if (strLength < 0) {
            return null;
        } else if (strLength == 0) {
            return "";
        }
        byte[] bytes = new byte[strLength];
        int readLen = inputStream.read(bytes, 0, strLength);
        if (readLen != strLength) {
            throw new IOException(String.format(RETURN_ERROR, strLength, readLen));
        }
        return new String(bytes, 0, strLength);
    }

    /** read string from byteBuffer. */
    public static String readString(ByteBuffer buffer) {
        int strLength = readInt(buffer);
        if (strLength < 0) {
            return null;
        } else if (strLength == 0) {
            return "";
        }
        byte[] bytes = new byte[strLength];
        buffer.get(bytes, 0, strLength);
        return new String(bytes, 0, strLength);
    }

    /** string length's type is varInt */
    public static String readVarIntString(ByteBuffer buffer) {
        int strLength = ReadWriteForEncodingUtils.readVarInt(buffer);
        if (strLength < 0) {
            return null;
        } else if (strLength == 0) {
            return "";
        }
        byte[] bytes = new byte[strLength];
        buffer.get(bytes, 0, strLength);
        return new String(bytes, 0, strLength);
    }

    /** read string from byteBuffer with user define length. */
    public static String readStringWithLength(ByteBuffer buffer, int length) {
        if (length < 0) {
            return null;
        } else if (length == 0) {
            return "";
        }
        byte[] bytes = new byte[length];
        buffer.get(bytes, 0, length);
        return new String(bytes, 0, length);
    }

    public static ByteBuffer getByteBuffer(String s) {
        return ByteBuffer.wrap(s.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }


    public static int write(Map<String, String> map, OutputStream stream) throws IOException {
        if (map == null) {
            return write(NO_BYTE_TO_READ, stream);
        }

        int length = 0;
        length += write(map.size(), stream);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            length += write(entry.getKey(), stream);
            length += write(entry.getValue(), stream);
        }
        return length;
    }

    public static void write(List<Map<String, String>> maps, OutputStream stream) throws IOException {
        for (Map<String, String> map : maps) {
            write(map, stream);
        }
    }

    public static int write(Map<String, String> map, ByteBuffer buffer) {
        if (map == null) {
            return write(NO_BYTE_TO_READ, buffer);
        }

        int length = 0;
        byte[] bytes;
        buffer.putInt(map.size());
        length += 4;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey() == null) {
                buffer.putInt(-1);
            } else {
                bytes = entry.getKey().getBytes();
                buffer.putInt(bytes.length);
                buffer.put(bytes);
                length += bytes.length;
            }
            length += 4;
            if (entry.getValue() == null) {
                buffer.putInt(-1);
            } else {
                bytes = entry.getValue().getBytes();
                buffer.putInt(bytes.length);
                buffer.put(bytes);
                length += bytes.length;
            }
            length += 4;
        }
        return length;
    }

    public static void write(List<Map<String, String>> maps, ByteBuffer buffer) {
        for (Map<String, String> map : maps) {
            write(map, buffer);
        }
    }

    /**
     * write a int value to outputStream according to flag. If flag is true, write 1, else write 0.
     */
    public static int write(Boolean flag, OutputStream outputStream) throws IOException {
        if (flag == null) {
            outputStream.write(-1);
        } else if (Boolean.TRUE.equals(flag)) {
            outputStream.write(1);
        } else {
            outputStream.write(0);
        }
        return BOOLEAN_LEN;
    }

    /** write a byte to byteBuffer according to flag. If flag is true, write 1, else write 0. */
    public static int write(Boolean flag, ByteBuffer buffer) {
        byte a;
        if (flag == null) {
            a = -1;
        } else if (Boolean.TRUE.equals(flag)) {
            a = 1;
        } else {
            a = 0;
        }

        buffer.put(a);
        return BOOLEAN_LEN;
    }

    /**
     * write a byte n.
     *
     * @return The number of bytes used to represent a {@code byte} value in two's complement binary
     *     form.
     */
    public static int write(byte n, OutputStream outputStream) throws IOException {
        outputStream.write(n);
        return Byte.BYTES;
    }

    /**
     * write a short n.
     *
     * @return The number of bytes used to represent n.
     */
    public static int write(short n, OutputStream outputStream) throws IOException {
        byte[] bytes = BytesUtils.shortToBytes(n);
        outputStream.write(bytes);
        return bytes.length;
    }

    /**
     * write a byte n to byteBuffer.
     *
     * @return The number of bytes used to represent a {@code byte} value in two's complement binary
     *     form.
     */
    public static int write(byte n, ByteBuffer buffer) {
        buffer.put(n);
        return Byte.BYTES;
    }

    /**
     * write a short n to byteBuffer.
     *
     * @return The number of bytes used to represent n.
     */
    public static int write(short n, ByteBuffer buffer) {
        buffer.putShort(n);
        return SHORT_LEN;
    }

    /**
     * write a short n to byteBuffer.
     *
     * @return The number of bytes used to represent n.
     */
    public static int write(Binary n, ByteBuffer buffer) {
        buffer.putInt(n.getLength());
        buffer.put(n.getValues());
        return INT_LEN + n.getLength();
    }


    /**
     * write a short n to byteBuffer.
     *
     * @return The number of bytes used to represent n.
     */
    public static int write(byte[] n, ByteBuffer buffer) {
        buffer.putInt(n.length);
        buffer.put(n);
        return INT_LEN + n.length;
    }

    /**
     * write a int n to outputStream.
     *
     * @return The number of bytes used to represent n.
     */
    public static int write(int n, OutputStream outputStream) throws IOException {
        byte[] bytes = BytesUtils.intToBytes(n);
        outputStream.write(bytes);
        return INT_LEN;
    }

    /** write the size (int) of the binary and then the bytes in binary */
    public static int write(Binary binary, OutputStream outputStream) throws IOException {
        byte[] size = BytesUtils.intToBytes(binary.getValues().length);
        outputStream.write(size);
        outputStream.write(binary.getValues());
        return size.length + binary.getValues().length;
    }

    /** write the size (int) of the binary and then the bytes in binary */
    public static int write(byte[] bytes, OutputStream outputStream) throws IOException {
        byte[] size = BytesUtils.intToBytes(bytes.length);
        outputStream.write(size);
        outputStream.write(bytes);
        return size.length + bytes.length;
    }

    /**
     * write a int n to byteBuffer.
     *
     * @return The number of bytes used to represent n.
     */
    public static int write(int n, ByteBuffer buffer) {
        buffer.putInt(n);
        return INT_LEN;
    }

    /**
     * write a float n to outputStream.
     *
     * @return The number of bytes used to represent n.
     */
    public static int write(float n, OutputStream outputStream) throws IOException {
        byte[] bytes = BytesUtils.floatToBytes(n);
        outputStream.write(bytes);
        return FLOAT_LEN;
    }

    /**
     * write a double n to outputStream.
     *
     * @return The number of bytes used to represent n.
     */
    public static int write(double n, OutputStream outputStream) throws IOException {
        byte[] bytes = BytesUtils.doubleToBytes(n);
        outputStream.write(bytes);
        return DOUBLE_LEN;
    }

    /**
     * write a long n to outputStream.
     *
     * @return The number of bytes used to represent n.
     */
    public static int write(long n, OutputStream outputStream) throws IOException {
        byte[] bytes = BytesUtils.longToBytes(n);
        outputStream.write(bytes);
        return LONG_LEN;
    }

    /** write a long n to byteBuffer. */
    public static int write(long n, ByteBuffer buffer) {
        buffer.putLong(n);
        return LONG_LEN;
    }

    /** write a float n to byteBuffer. */
    public static int write(float n, ByteBuffer buffer) {
        buffer.putFloat(n);
        return FLOAT_LEN;
    }

    /** write a double n to byteBuffer. */
    public static int write(double n, ByteBuffer buffer) {
        buffer.putDouble(n);
        return DOUBLE_LEN;
    }

    /**
     * write string to outputStream.
     *
     * @return the length of string represented by byte[].
     */
    public static int write(String s, OutputStream outputStream) throws IOException {
        int len = 0;
        if (s == null) {
            len += write(NO_BYTE_TO_READ, outputStream);
            return len;
        }

        byte[] bytes = s.getBytes();
        len += write(bytes.length, outputStream);
        outputStream.write(bytes);
        len += bytes.length;
        return len;
    }

    /**
     * write string to outputStream.
     *
     * @return the length of string represented by byte[].
     */
    public static int writeVar(String s, OutputStream outputStream) throws IOException {
        int len = 0;
        if (s == null) {
            len += ReadWriteForEncodingUtils.writeVarInt(NO_BYTE_TO_READ, outputStream);
            return len;
        }

        byte[] bytes = s.getBytes(STRING_CHARSET);
        len += ReadWriteForEncodingUtils.writeVarInt(bytes.length, outputStream);
        outputStream.write(bytes);
        len += bytes.length;
        return len;
    }

    /**
     * write string to byteBuffer.
     *
     * @return the length of string represented by byte[].
     */
    public static int write(String s, ByteBuffer buffer) {
        if (s == null) {
            return write(NO_BYTE_TO_READ, buffer);
        }
        int len = 0;
        byte[] bytes = s.getBytes();
        len += write(bytes.length, buffer);
        buffer.put(bytes);
        len += bytes.length;
        return len;
    }

    public static int writeVar(String s, ByteBuffer buffer) {
        if (s == null) {
            return ReadWriteForEncodingUtils.writeVarInt(NO_BYTE_TO_READ, buffer);
        }
        int len = 0;
        byte[] bytes = s.getBytes();
        len += ReadWriteForEncodingUtils.writeVarInt(bytes.length, buffer);
        buffer.put(bytes);
        len += bytes.length;
        return len;
    }

    /** write byteBuffer.capacity and byteBuffer.array to outputStream. */
    public static int write(ByteBuffer byteBuffer, OutputStream outputStream) throws IOException {
        int len = 0;
        len += write(byteBuffer.capacity(), outputStream);
        byte[] bytes = byteBuffer.array();
        outputStream.write(bytes);
        len += bytes.length;
        return len;
    }

    public static void writeWithoutSize(
            ByteBuffer byteBuffer, int offset, int len, OutputStream outputStream) throws IOException {
        byte[] bytes = byteBuffer.array();
        outputStream.write(bytes, offset, len);
    }

    /** write byteBuffer.capacity and byteBuffer.array to byteBuffer. */
    public static int write(ByteBuffer byteBuffer, ByteBuffer buffer) {
        int len = 0;
        len += write(byteBuffer.capacity(), buffer);
        byte[] bytes = byteBuffer.array();
        buffer.put(bytes);
        len += bytes.length;
        return len;
    }



    /** TSDataType. */
    public static int write(DataType dataType, OutputStream outputStream) throws IOException {
        return write(dataType.serialize(), outputStream);
    }

    public static int write(DataType dataType, ByteBuffer buffer) {
        return write(dataType.serialize(), buffer);
    }



}
