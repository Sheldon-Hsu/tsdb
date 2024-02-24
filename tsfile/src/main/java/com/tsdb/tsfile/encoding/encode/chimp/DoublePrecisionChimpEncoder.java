package com.tsdb.tsfile.encoding.encode.chimp;

import com.tsdb.tsfile.encoding.encode.chimp.LongChimpEncoder;

import java.io.ByteArrayOutputStream;

public class DoublePrecisionChimpEncoder extends LongChimpEncoder {

  private static final long CHIMP_ENCODING_ENDING = Double.doubleToRawLongBits(Double.NaN);

  @Override
  public final void encode(double value, ByteArrayOutputStream out) {
    encode(Double.doubleToRawLongBits(value), out);
  }

  @Override
  public void flush(ByteArrayOutputStream out) {
    // ending stream
    encode(CHIMP_ENCODING_ENDING, out);

    // flip the byte no matter it is empty or not
    // the empty ending byte is necessary when decoding
    bitsLeft = 0;
    flipByte(out);

    // the encoder may be reused, so let us reset it
    reset();
  }
}
