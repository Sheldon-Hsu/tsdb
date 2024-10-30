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

package com.tsdb.jdbc.server.exception.service;

import com.tsdb.jdbc.common.exception.TSDBException;

public class StartupException extends TSDBException {
    private static final long serialVersionUID = -497368726126559138L;

    public StartupException(String message, int errorCode) {
        super(message, errorCode);
    }

    public StartupException(String message, int errorCode, boolean isUserException) {
        super(message, errorCode, isUserException);
    }

    public StartupException(String message, Throwable cause, int errorCode) {
        super(message, cause, errorCode);
    }

    public StartupException(Throwable cause, int errorCode) {
        super(cause, errorCode);
    }

    public StartupException(Throwable cause, int errorCode, boolean isUserException) {
        super(cause, errorCode, isUserException);
    }
}
