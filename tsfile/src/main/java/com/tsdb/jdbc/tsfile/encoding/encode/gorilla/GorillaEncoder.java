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

package com.tsdb.jdbc.tsfile.encoding.encode.gorilla;

import com.tsdb.jdbc.tsfile.encoding.TSEncoding;
import com.tsdb.jdbc.tsfile.encoding.encode.Encoder;

import java.io.ByteArrayOutputStream;

public abstract class GorillaEncoder extends Encoder {

  protected boolean firstValueWasWritten = false;
  protected int storedLeadingZeros = Integer.MAX_VALUE;
  protected int storedTrailingZeros = 0;

  private byte buffer = 0;
  protected int bitsLeft = Byte.SIZE;

  protected GorillaEncoder() {
    super(TSEncoding.GORILLA);
  }

  @Override
  public final long getMaxByteSize() {
    return 0;
  }

  protected void reset() {
    firstValueWasWritten = false;
    storedLeadingZeros = Integer.MAX_VALUE;
    storedTrailingZeros = 0;

    buffer = 0;
    bitsLeft = Byte.SIZE;
  }

  /** Stores a 0 and increases the count of bits by 1. */
  protected void skipBit(ByteArrayOutputStream out) {
    bitsLeft--;
    flipByte(out);
  }

  /** Stores a 1 and increases the count of bits by 1. */
  protected void writeBit(ByteArrayOutputStream out) {
    buffer |= (1 << (bitsLeft - 1));
    bitsLeft--;
    flipByte(out);
  }

  /**
   * Writes the given long value using the defined amount of least significant bits.
   *
   * @param value The long value to be written
   * @param bits How many bits are stored to the stream
   */
  protected void writeBits(long value, int bits, ByteArrayOutputStream out) {
    while (bits > 0) {
      int shift = bits - bitsLeft;
      if (shift >= 0) {
        buffer |= (byte) ((value >> shift) & ((1 << bitsLeft) - 1));
        bits -= bitsLeft;
        bitsLeft = 0;
      } else {
        shift = bitsLeft - bits;
        buffer |= (byte) (value << shift);
        bitsLeft -= bits;
        bits = 0;
      }
      flipByte(out);
    }
  }

  protected void flipByte(ByteArrayOutputStream out) {
    if (bitsLeft == 0) {
      out.write(buffer);
      buffer = 0;
      bitsLeft = Byte.SIZE;
    }
  }
}
