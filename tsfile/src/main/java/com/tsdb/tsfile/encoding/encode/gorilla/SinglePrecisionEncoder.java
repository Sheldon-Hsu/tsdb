package com.tsdb.tsfile.encoding.encode.gorilla;

import java.io.ByteArrayOutputStream;

import static com.tsdb.tsfile.conf.TSFileConfig.GORILLA_ENCODING_ENDING_FLOAT;

public class SinglePrecisionEncoder extends IntGorillaEncoder {

  @Override
  public final void encode(float value, ByteArrayOutputStream out) {
    encode(Float.floatToRawIntBits(value), out);
  }

  @Override
  public void flush(ByteArrayOutputStream out) {
    // ending stream
    encode(GORILLA_ENCODING_ENDING_FLOAT, out);

    // flip the byte no matter it is empty or not
    // the empty ending byte is necessary when decoding
    bitsLeft = 0;
    flipByte(out);

    // the encoder may be reused, so let us reset it
    reset();
  }
}
