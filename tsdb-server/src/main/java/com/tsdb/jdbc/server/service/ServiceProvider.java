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
package com.tsdb.jdbc.server.service;

import com.tsdb.jdbc.common.config.TSDBMessage;
import com.tsdb.jdbc.rpc.TSStatusCode;
import com.tsdb.jdbc.server.control.SessionManager;
import com.tsdb.jdbc.server.service.login.SessionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceProvider {
    private static final Logger logger = LoggerFactory.getLogger(ServiceProvider.class);
    public SessionManager sessionManager = SessionManager.getInstance();

    public SessionStatus openSession(String database, String username, String password, String host, String zoneId) {
        SessionStatus tsdbStatus = new SessionStatus();
        boolean status = false;
        long sessionId = sessionManager.requestSessionId(username);


        status = true;
        if (status){
            tsdbStatus.setCode(TSStatusCode.SUCCESS_STATUS.getStatusCode());
            tsdbStatus.setMessage(TSDBMessage.LOGIN_SUCCESS);
        }else {
            tsdbStatus.setCode(TSStatusCode.WRONG_LOGIN_PASSWORD_ERROR.getStatusCode());
            tsdbStatus.setMessage(TSDBMessage.AUTH_FAIL);
        }
        logger.info("login status :{}. User: {},opens session:{}.",
                status,
                username,
                sessionId);
        tsdbStatus.setSessionId(sessionId);

        return tsdbStatus;
    }

    public static ServiceProvider getInstance() {
        return ServiceProvider.ServiceProviderHelper.INSTANCE;
    }

    private static class ServiceProviderHelper {
        private static final ServiceProvider INSTANCE = new ServiceProvider();

        private ServiceProviderHelper() {
        }
    }
}
