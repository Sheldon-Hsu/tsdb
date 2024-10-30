/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tsdb.jdbc.rpc;

import com.tsdb.common.rpc.thrift.TSDBStatus;
import com.tsdb.rpc.thrift.TSDBRpcService;
import com.tsdb.rpc.thrift.TSExecuteStatementResp;
import com.tsdb.rpc.thrift.TSFetchResultsResp;

import java.lang.reflect.Proxy;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class RpcUtils {

  /** How big should the default read and write buffers be? Defaults to 1KB */
  public static final int THRIFT_DEFAULT_BUF_CAPACITY = 1024;
  /**
   * It is used to prevent the size of the parsing package from being too large and allocating the
   * buffer will cause oom. Therefore, the maximum length of the requested memory is limited when
   * reading. Thrift max frame size (16384000 bytes by default), we change it to 512MB.
   */
  public static final int THRIFT_FRAME_MAX_SIZE = 536870912;

  /**
   * if resizeIfNecessary is called continuously with a small size for more than
   * MAX_BUFFER_OVERSIZE_TIME times, we will shrink the buffer to reclaim space.
   */
  public static final int MAX_BUFFER_OVERSIZE_TIME = 5;

  public static final long MIN_SHRINK_INTERVAL = 60_000L;

  private RpcUtils() {
    // util class
  }

  public static final TSDBStatus SUCCESS_STATUS =
      new TSDBStatus(TSStatusCode.SUCCESS_STATUS.getStatusCode());

  public static TSDBRpcService.Iface newSynchronizedClient(TSDBRpcService.Iface client) {
    return (TSDBRpcService.Iface)
        Proxy.newProxyInstance(
            RpcUtils.class.getClassLoader(),
            new Class[] {TSDBRpcService.Iface.class},
            new SynchronizedHandler(client));
  }

  /**
   * verify success.
   *
   * @param status -status
   */
  public static void verifySuccess(TSDBStatus status) throws StatementExecutionException {

    if (status.code != TSStatusCode.SUCCESS_STATUS.getStatusCode()) {
      throw new StatementExecutionException(status);
    }
  }



  /** convert from TSStatusCode to TSStatus according to status code and status message */
  public static TSDBStatus getStatus(TSStatusCode tsStatusCode) {
    return new TSDBStatus(tsStatusCode.getStatusCode());
  }



  /**
   * convert from TSStatusCode to TSStatus, which has message appending with existed status message
   *
   * @param tsStatusCode status type
   * @param message appending message
   */
  public static TSDBStatus getStatus(TSStatusCode tsStatusCode, String message) {
    TSDBStatus status = new TSDBStatus(tsStatusCode.getStatusCode());
    status.setMessage(message);
    return status;
  }

  public static TSDBStatus getStatus(int code, String message) {
    TSDBStatus status = new TSDBStatus(code);
    status.setMessage(message);
    return status;
  }

  public static TSExecuteStatementResp getTSExecuteStatementResp(TSStatusCode tsStatusCode) {
    TSDBStatus status = getStatus(tsStatusCode);
    return getTSExecuteStatementResp(status);
  }

  public static TSExecuteStatementResp getTSExecuteStatementResp(
      TSStatusCode tsStatusCode, String message) {
    TSDBStatus status = getStatus(tsStatusCode, message);
    return getTSExecuteStatementResp(status);
  }

  public static TSExecuteStatementResp getTSExecuteStatementResp(TSDBStatus status) {
    TSExecuteStatementResp resp = new TSExecuteStatementResp();
    TSDBStatus tsStatus = new TSDBStatus(status);
    resp.setStatus(tsStatus);
    return resp;
  }

  public static TSFetchResultsResp getTSFetchResultsResp(TSStatusCode tsStatusCode) {
    TSDBStatus status = getStatus(tsStatusCode);
    return getTSFetchResultsResp(status);
  }

  public static TSFetchResultsResp getTSFetchResultsResp(
      TSStatusCode tsStatusCode, String appendMessage) {
    TSDBStatus status = getStatus(tsStatusCode, appendMessage);
    return getTSFetchResultsResp(status);
  }

  public static TSFetchResultsResp getTSFetchResultsResp(TSDBStatus status) {
    TSFetchResultsResp resp = new TSFetchResultsResp();
    TSDBStatus tsStatus = new TSDBStatus(status);
    resp.setStatus(tsStatus);
    return resp;
  }

  public static final String DEFAULT_TIME_FORMAT = "default";
  public static final String DEFAULT_TIMESTAMP_PRECISION = "ms";

  public static String setTimeFormat(String newTimeFormat) {
    String timeFormat;
    switch (newTimeFormat.trim().toLowerCase()) {
      case "long":
      case "number":
      case DEFAULT_TIME_FORMAT:
      case "iso8601":
        timeFormat = newTimeFormat.trim().toLowerCase();
        break;
      default:
        // use java default SimpleDateFormat to check whether input time format is legal
        // if illegal, it will throw an exception
        new SimpleDateFormat(newTimeFormat.trim());
        timeFormat = newTimeFormat;
        break;
    }
    return timeFormat;
  }

  public static String formatDatetime(
      String timeFormat, String timePrecision, long timestamp, ZoneId zoneId) {
    ZonedDateTime dateTime;
    switch (timeFormat) {
      case "long":
      case "number":
        return Long.toString(timestamp);
      case DEFAULT_TIME_FORMAT:
      case "iso8601":
        return parseLongToDateWithPrecision(
            DateTimeFormatter.ISO_OFFSET_DATE_TIME, timestamp, zoneId, timePrecision);
      default:
        dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), zoneId);
        return dateTime.format(DateTimeFormatter.ofPattern(timeFormat));
    }
  }

  public static String formatDatetimeStr(String datetime, StringBuilder digits) {
    if (datetime.contains("+")) {
      String timeZoneStr = datetime.substring(datetime.length() - 6);
      return datetime.substring(0, datetime.length() - 6) + "." + digits + timeZoneStr;
    } else if (datetime.contains("Z")) {
      String timeZoneStr = datetime.substring(datetime.length() - 1);
      return datetime.substring(0, datetime.length() - 1) + "." + digits + timeZoneStr;
    } else {
      String timeZoneStr = "";
      return datetime + "." + digits + timeZoneStr;
    }
  }

  @SuppressWarnings("squid:S3776") // Suppress high Cognitive Complexity warning
  public static String parseLongToDateWithPrecision(
      DateTimeFormatter formatter, long timestamp, ZoneId zoneid, String timestampPrecision) {
    long integerOfDate;
    StringBuilder digits;
    if ("ms".equals(timestampPrecision)) {
      if (timestamp > 0 || timestamp % 1000 == 0) {
        integerOfDate = timestamp / 1000;
        digits = new StringBuilder(Long.toString(timestamp % 1000));
      } else {
        integerOfDate = timestamp / 1000 - 1;
        digits = new StringBuilder(Long.toString(1000 + timestamp % 1000));
      }
      ZonedDateTime dateTime =
          ZonedDateTime.ofInstant(Instant.ofEpochSecond(integerOfDate), zoneid);
      String datetime = dateTime.format(formatter);
      int length = digits.length();
      if (length != 3) {
        for (int i = 0; i < 3 - length; i++) {
          digits.insert(0, "0");
        }
      }
      return formatDatetimeStr(datetime, digits);
    } else if ("us".equals(timestampPrecision)) {
      if (timestamp > 0 || timestamp % 1000_000 == 0) {
        integerOfDate = timestamp / 1000_000;
        digits = new StringBuilder(Long.toString(timestamp % 1000_000));
      } else {
        integerOfDate = timestamp / 1000_000 - 1;
        digits = new StringBuilder(Long.toString(1000_000 + timestamp % 1000_000));
      }
      ZonedDateTime dateTime =
          ZonedDateTime.ofInstant(Instant.ofEpochSecond(integerOfDate), zoneid);
      String datetime = dateTime.format(formatter);
      int length = digits.length();
      if (length != 6) {
        for (int i = 0; i < 6 - length; i++) {
          digits.insert(0, "0");
        }
      }
      return formatDatetimeStr(datetime, digits);
    } else {
      if (timestamp > 0 || timestamp % 1000_000_000L == 0) {
        integerOfDate = timestamp / 1000_000_000L;
        digits = new StringBuilder(Long.toString(timestamp % 1000_000_000L));
      } else {
        integerOfDate = timestamp / 1000_000_000L - 1;
        digits = new StringBuilder(Long.toString(1000_000_000L + timestamp % 1000_000_000L));
      }
      ZonedDateTime dateTime =
          ZonedDateTime.ofInstant(Instant.ofEpochSecond(integerOfDate), zoneid);
      String datetime = dateTime.format(formatter);
      int length = digits.length();
      if (length != 9) {
        for (int i = 0; i < 9 - length; i++) {
          digits.insert(0, "0");
        }
      }
      return formatDatetimeStr(datetime, digits);
    }
  }

}
