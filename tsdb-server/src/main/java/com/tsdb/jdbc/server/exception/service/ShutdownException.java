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
package com.tsdb.jdbc.server.exception.service;


import com.tsdb.jdbc.rpc.TSStatusCode;
import com.tsdb.jdbc.common.exception.TSDBException;

public class ShutdownException extends TSDBException {

  private static final long serialVersionUID = 8729999929155218308L;

  public ShutdownException(String message, int errorCode) {
    super(message, errorCode);
  }

  public ShutdownException(Throwable cause) {
    super(cause.getMessage(), TSStatusCode.SHUT_DOWN_ERROR.getStatusCode());
  }

  public ShutdownException(String message, Throwable cause, int errorCode) {
    super(message, cause, errorCode);
  }

  public ShutdownException(Throwable cause, int errorCode) {
    super(cause, errorCode);
  }
}
