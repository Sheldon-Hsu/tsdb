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

package com.tsdb.jdbc.common.exception;

public class TSDBException extends Exception {

  private static final long serialVersionUID = 8480450962311247736L;
  protected int errorCode;

  protected boolean isUserException = false;

  public TSDBException(String message, int errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public TSDBException(String message, int errorCode, boolean isUserException) {
    super(message);
    this.errorCode = errorCode;
    this.isUserException = isUserException;
  }

  public TSDBException(String message, Throwable cause, int errorCode) {
    super(message, cause);
    this.errorCode = errorCode;
  }

  public TSDBException(Throwable cause, int errorCode) {
    super(cause);
    this.errorCode = errorCode;
  }

  public TSDBException(Throwable cause, int errorCode, boolean isUserException) {
    super(cause);
    this.errorCode = errorCode;
    this.isUserException = isUserException;
  }

  public boolean isUserException() {
    return isUserException;
  }

  public int getErrorCode() {
    return errorCode;
  }
}
