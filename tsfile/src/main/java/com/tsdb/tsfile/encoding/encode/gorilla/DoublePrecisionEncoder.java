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

package com.tsdb.tsfile.encoding.encode.gorilla;

import java.io.ByteArrayOutputStream;

import static com.tsdb.tsfile.conf.TSFileConfig.GORILLA_ENCODING_ENDING_DOUBLE;


public class DoublePrecisionEncoder extends LongGorillaEncoder {

  @Override
  public final void encode(double value, ByteArrayOutputStream out) {
    encode(Double.doubleToRawLongBits(value), out);
  }

  @Override
  public void flush(ByteArrayOutputStream out) {
    // ending stream
    encode(GORILLA_ENCODING_ENDING_DOUBLE, out);

    // flip the byte no matter it is empty or not
    // the empty ending byte is necessary when decoding
    bitsLeft = 0;
    flipByte(out);

    // the encoder may be reused, so let us reset it
    reset();
  }
}
