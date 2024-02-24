package com.tsdb.common.exception;

public class UnSupportedDataTypeException extends RuntimeException {

  private static final long serialVersionUID = -6742538826992053091L;

  public UnSupportedDataTypeException(String message) {
    super("Unsupported data type: " + message);
  }

  public UnSupportedDataTypeException(String message, Throwable e) {
    super(message + e.getMessage());
  }
}
