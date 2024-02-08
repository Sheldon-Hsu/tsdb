package com.tsdb.tsfile.encode;

public enum TSEncoding {
  PLAIN((byte) 0);

  private final byte type;

  TSEncoding(byte type) {
    this.type = type;
  }

  /**
   *deserialize encoding deserialize type.
   *
   * @param encoding -use to determine encoding type
   * @return -encoding type
   */
  public static TSEncoding deserialize(byte encoding) {
    return getTsEncoding(encoding);
  }

  /**
   * serialize encoding type.
   *
   * @return -encoding type
   */
  public byte serialize() {
    return type;
  }

  private static TSEncoding getTsEncoding(byte encoding) {
    switch (encoding) {
      case 0:
        return TSEncoding.PLAIN;
      default:
        throw new IllegalArgumentException("unsupported encoding type: " + encoding);
    }
  }

  public static int getSerializedSize() {
    return Byte.BYTES;
  }


}
