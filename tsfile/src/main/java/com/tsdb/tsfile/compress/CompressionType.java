package com.tsdb.tsfile.compress;

public enum CompressionType {
  /** Do not comprocess. */
  UNCOMPRESSED("", (byte) 0),

  /** SNAPPY. */
  SNAPPY(".snappy", (byte) 1),

  /** GZIP. */
  GZIP(".gzip", (byte) 2),

  /** LZ4. */
  // NOTICE: To ensure the compatibility of existing files, do not change the byte LZ4 binds to.
  LZ4(".lz4", (byte) 7),

  /** ZSTD. */
  ZSTD(".zstd", (byte) 8),

  /** LZMA2. */
  LZMA2(".lzma2", (byte) 9);

  private final String extensionName;
  private final byte index;

  CompressionType(String extensionName, byte index) {
    this.extensionName = extensionName;
    this.index = index;
  }

  /**
   * deserialize byte number.
   *
   * @param compressor byte number
   * @return CompressionType
   * @throws IllegalArgumentException illegal argument
   */
  public static CompressionType deserialize(byte compressor) {
    switch (compressor) {
      case 0:
        return CompressionType.UNCOMPRESSED;
      case 1:
        return CompressionType.SNAPPY;
      case 2:
        return CompressionType.GZIP;
      case 7:
        return CompressionType.LZ4;
      case 8:
        return CompressionType.ZSTD;
      case 9:
        return CompressionType.LZMA2;
      default:
        throw new IllegalArgumentException("Invalid input: " + compressor);
    }
  }

  public static int getSerializedSize() {
    return Byte.BYTES;
  }

  /**
   * get serialized size.
   *
   * @return byte of index
   */
  public byte serialize() {
    return this.index;
  }

  /**
   * get extension.
   *
   * @return extension (string type), for example: .snappy, .gz, .lzo
   */
  public String getExtension() {
    return extensionName;
  }
}
