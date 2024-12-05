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

import com.tsdb.jdbc.common.config.TSDBConstant;
import com.tsdb.server.exception.service.StartupException;
import com.tsdb.server.service.IService;
import com.tsdb.server.service.ServiceID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RPCService implements IService {
    private static final Logger logger = LoggerFactory.getLogger(RPCService.class);
    private static final String STATUS_UP = "UP";
    private static final String STATUS_DOWN = "DOWN";
    private ThriftServiceThread rpcServiceThread;


    @Override
    public void start() throws StartupException {
        if (STATUS_UP.equals(getRPCServiceStatus())) {
            logger.info("{}: has en already running.", TSDBConstant.GLOBAL_DB_NAME);
            return;
        }
        logger.info("RPCService start...");
        try {
            rpcServiceThread = new ThriftServiceThread();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        rpcServiceThread.start();
        logger.info("RPCService started");
    }

    @Override
    public void stop() {

    }

    @Override
    public void shutdownNow() {

    }


    private void startService() {

    }

    private void initProcessor() {

    }

    private String getRPCServiceStatus() {
        if (rpcServiceThread == null){
            return STATUS_DOWN;
        }else {
            return STATUS_UP;
        }

    }

    @Override
    public ServiceID getServiceID() {
        return ServiceID.RPC_SERVICE;
    }

    public static RPCService getInstance() {
        return RPCService.InstanceHolder.INSTANCE;
    }



    private static class InstanceHolder {

        private InstanceHolder() {
        }

        private static final RPCService INSTANCE = new RPCService();
    }

}
