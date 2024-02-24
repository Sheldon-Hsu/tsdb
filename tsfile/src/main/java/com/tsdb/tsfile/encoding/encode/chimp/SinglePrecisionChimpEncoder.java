package com.tsdb.tsfile.encoding.encode.chimp;



import java.io.ByteArrayOutputStream;

public class SinglePrecisionChimpEncoder extends IntChimpEncoder {

  private static final int CHIMP_ENCODING_ENDING = Float.floatToRawIntBits(Float.NaN);

  @Override
  public final void encode(float value, ByteArrayOutputStream out) {
    encode(Float.floatToRawIntBits(value), out);
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
