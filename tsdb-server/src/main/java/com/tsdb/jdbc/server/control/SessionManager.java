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
package com.tsdb.jdbc.server.control;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class SessionManager {
    private final ThreadLocal<Long> sessionId = new ThreadLocal<>();
    private final AtomicLong idGenerator =new AtomicLong();
    private final Map<Long,String> idToUser = new ConcurrentHashMap<>();

    private SessionManager() {
    }


    public Long getCurSessionId(){
        return sessionId.get();
    }

    public void removeCurSessionId(){
        sessionId.remove();
    }


    public long requestSessionId(String username){
       long id= idGenerator.incrementAndGet();
       sessionId.set(id);
       idToUser.put(id,username);
       return id;
    }


    public static SessionManager getInstance() {
        return SessionManagerHelper.INSTANCE;
    }

    private static class SessionManagerHelper {
        private static final SessionManager INSTANCE = new SessionManager();

        private SessionManagerHelper() {
        }
    }
}
