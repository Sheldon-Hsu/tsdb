
package com.tsdb.tsfile.exception.filter;


import com.tsdb.tsfile.exception.TsFileRuntimeException;

public class StatisticsClassException extends TsFileRuntimeException {

  private static final long serialVersionUID = -5445795844780183770L;

  public StatisticsClassException(Class<?> className1, Class<?> className2) {
    super("Statistics classes mismatched: " + className1 + " vs. " + className2);
  }

  public StatisticsClassException(String message) {
    super(message);
  }
}
