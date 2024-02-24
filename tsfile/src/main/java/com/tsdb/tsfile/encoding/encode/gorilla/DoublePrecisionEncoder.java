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
