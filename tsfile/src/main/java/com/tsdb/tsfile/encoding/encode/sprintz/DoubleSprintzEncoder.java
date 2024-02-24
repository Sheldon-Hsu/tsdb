package com.tsdb.tsfile.encoding.encode.sprintz;

import com.tsdb.tsfile.encoding.bitpacking.LongPacker;
import com.tsdb.tsfile.encoding.encode.gorilla.DoublePrecisionEncoder;
import com.tsdb.tsfile.encoding.fire.LongFire;
import com.tsdb.tsfile.exception.encode.TsFileEncodingException;
import com.tsdb.tsfile.utils.ReadWriteForEncodingUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Vector;

public class DoubleSprintzEncoder extends SprintzEncoder {

  // bit packer
  LongPacker packer;

  // Long Fire Predictor
  LongFire firePred;

  // we save all value in a list and calculate its bitWidth
  protected Vector<Double> values;

  // convert to Long Buffer
  long[] convertBuffer;

  public DoubleSprintzEncoder() {
    super();
    values = new Vector<>();
    firePred = new LongFire(3);
    convertBuffer = new long[Block_size];
  }

  @Override
  protected void reset() {
    super.reset();
    values.clear();
  }

  @Override
  public int getOneItemMaxSize() {
    return 1 + (1 + Block_size) * Double.BYTES;
  }

  @Override
  public long getMaxByteSize() {
    return 1 + (long) (values.size() + 1) * Double.BYTES;
  }

  protected long predict(Double value, Double preVlaue) throws TsFileEncodingException {
    long pred;
    if (predictMethod.equals("delta")) {
      pred = delta(value, preVlaue);
    } else if (predictMethod.equals("fire")) {
      pred = fire(value, preVlaue);
    } else {
      throw new TsFileEncodingException(
          "Config: Predict Method {} of SprintzEncoder is not supported.");
    }
    if (pred <= 0) {
      pred = -2 * pred;
    } else {
      pred = 2 * pred - 1; // TODO:overflow
    }
    return pred;
  }

  @Override
  protected void bitPack() throws IOException {
    final double preValue = values.get(0);
    values.remove(0);
    Vector<Long> convertBufferList = new Vector<>();
    for (long itemBuffer : convertBuffer) {
      convertBufferList.add(itemBuffer);
    }
    this.bitWidth = ReadWriteForEncodingUtils.getLongMaxBitWidth(convertBufferList);
    packer = new LongPacker(this.bitWidth);
    byte[] bytes = new byte[bitWidth];
    packer.pack8Values(convertBuffer, 0, bytes);
    ReadWriteForEncodingUtils.writeIntLittleEndianPaddedOnBitWidth(bitWidth, byteCache, 1);
    byteCache.write(ByteBuffer.allocate(8).putDouble(preValue).array());
    byteCache.write(bytes, 0, bytes.length);
  }

  protected long delta(Double value, Double preValue) {
    return Double.doubleToLongBits(value) - Double.doubleToLongBits(preValue);
  }

  protected long fire(Double value, Double preValue) {
    long prev = Double.doubleToLongBits(preValue);
    long val = Double.doubleToLongBits(value);
    long pred = firePred.predict(prev);
    long err = val - pred;
    firePred.train(prev, val, err);
    return err;
  }

  @Override
  public void flush(ByteArrayOutputStream out) throws IOException {
    if (byteCache.size() > 0) {
      byteCache.writeTo(out);
    }
    if (!values.isEmpty()) {
      int size = values.size();
      size |= (1 << 7);
      ReadWriteForEncodingUtils.writeIntLittleEndianPaddedOnBitWidth(size, out, 1);
      DoublePrecisionEncoder encoder = new DoublePrecisionEncoder();
      for (double val : values) {
        encoder.encode(val, out);
      }
      encoder.flush(out);
    }
    reset();
  }

  @Override
  public void encode(double value, ByteArrayOutputStream out) {
    if (!isFirstCached) {
      values.add(value);
      isFirstCached = true;
      return;
    } else {
      values.add(value);
    }
    if (values.size() == Block_size + 1) {
      try {
        firePred.reset();
        for (int i = 1; i <= Block_size; i++) {
          convertBuffer[i - 1] = predict(values.get(i), values.get(i - 1));
        }
        bitPack();
        isFirstCached = false;
        values.clear();
        groupNum++;
        if (groupNum == groupMax) {
          flush(out);
        }
      } catch (IOException e) {
        logger.error("Error occured when encoding INT32 Type value with with Sprintz", e);
      }
    }
  }
}
