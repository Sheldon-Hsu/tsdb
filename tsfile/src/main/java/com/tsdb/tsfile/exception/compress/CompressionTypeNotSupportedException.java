package com.tsdb.tsfile.exception.compress;

/**
 * This exception will be thrown when the codec is not supported by tsfile, meaning there is no
 * matching type defined in CompressionCodecName.
 */
public class CompressionTypeNotSupportedException extends RuntimeException {

  private static final long serialVersionUID = -8659367946905193448L;
  private final Class<?> codecClass;

  public CompressionTypeNotSupportedException(String codecType) {
    super("codec not supported: " + codecType);
    this.codecClass = null;
  }

  public Class<?> getCodecClass() {
    return codecClass;
  }
}
