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

package com.tsdb.jdbc.server;

import com.tsdb.jdbc.common.config.TSDBConstant;
import com.tsdb.jdbc.server.flush.FlushManager;
import com.tsdb.jdbc.server.service.thrift.RPCService;
import com.tsdb.jdbc.server.exception.service.StartupException;
import com.tsdb.jdbc.server.query.QueryEngine;
import com.tsdb.jdbc.server.service.ServiceRegister;
import com.tsdb.jdbc.server.storage.StorageEngine;
import com.tsdb.jdbc.server.storage.region.RegionServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TSDB {
    private static final Logger logger = LoggerFactory.getLogger(TSDB.class);
    private static final ServiceRegister register = new ServiceRegister();

    public static void main(String[] args) {

        try {
            TSDB tsdb = TSDB.getInstance();
            tsdb.setUp();
        } catch (StartupException e) {
            logger.error("start error:", e);
            logger.error("{} exit", TSDBConstant.GLOBAL_DB_NAME);
            System.exit(1);
        }
    }


    private void setUp() throws StartupException {
        register.register(StorageEngine.getInstance());
        register.register(FlushManager.getInstance());
        register.register(QueryEngine.getInstance());
        register.register(RegionServer.getInstance());
        register.register(RPCService.getInstance());
    }

    public static TSDB getInstance() {
        return TSDBHolder.INSTANCE;
    }

    private static class TSDBHolder {

        private static final TSDB INSTANCE = new TSDB();

        private TSDBHolder() {
        }
    }
}