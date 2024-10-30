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

package com.tsdb.jdbc.server.exception;


import com.tsdb.jdbc.rpc.TSStatusCode;
import com.tsdb.jdbc.common.exception.TSDBException;

public class WriteProcessException extends TSDBException {

  private static final long serialVersionUID = 7082567513626836322L;

  public WriteProcessException(String message) {
    super(message, TSStatusCode.WRITE_PROCESS_ERROR.getStatusCode());
  }

  public WriteProcessException(String message, int errorCode) {
    super(message, errorCode);
  }

  public WriteProcessException(String message, int errorCode, boolean isUserException) {
    super(message, errorCode, isUserException);
  }

  public WriteProcessException(String message, Throwable cause) {
    super(message, cause, TSStatusCode.WRITE_PROCESS_ERROR.getStatusCode());
  }

  public WriteProcessException(Exception exception) {
    super(exception, TSStatusCode.WRITE_PROCESS_ERROR.getStatusCode());
  }
}