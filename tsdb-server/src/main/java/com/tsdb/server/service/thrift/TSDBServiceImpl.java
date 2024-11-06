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
package com.tsdb.server.service.thrift;

import com.tsdb.common.rpc.thrift.TSDBStatus;
import com.tsdb.server.service.ServiceProvider;
import com.tsdb.server.service.login.SessionStatus;
import com.tsdb.rpc.thrift.*;
import org.apache.thrift.TException;

public class TSDBServiceImpl implements TSDBRpcService.Iface {
    private static final ServiceProvider serviceProvider = ServiceProvider.getInstance();

    @Override
    public TSOpenSessionResp openSession(TSOpenSessionReq request) throws TException {
        String username = request.getUsername();
        String passwd  =request.getPassword();
        String zoneId = request.getZoneId();
        String database = request.getDatabase();
        String host = request.getHost();
        SessionStatus sessionStatus = serviceProvider.openSession(database,username,passwd,host,zoneId);
        TSOpenSessionResp resp = new TSOpenSessionResp(sessionStatus);
        resp.setSessionId(sessionStatus.getSessionId());
        return resp;
    }

    @Override
    public TSDBStatus closeSession(TSCloseSessionReq request) throws TException {
        return null;
    }

    @Override
    public TSDBStatus insertData(InsertDataReq request) throws TException {

        return null;
    }
}
