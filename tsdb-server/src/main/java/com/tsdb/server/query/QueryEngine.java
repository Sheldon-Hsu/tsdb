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

package com.tsdb.server.query;

import com.tsdb.server.concurrent.threadpool.QueryTaskPoolManager;
import com.tsdb.server.service.IService;
import com.tsdb.server.service.ServiceID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryEngine implements IService {
    private static final Logger logger = LoggerFactory.getLogger(QueryEngine.class);
    private final QueryTaskPoolManager pool = QueryTaskPoolManager.getInstance();


    @Override
    public void start() {
        logger.info("StorageEngine start...");
        pool.start();
    }

    @Override
    public void stop() {
        logger.info("QueryEngine stop...");
    }

    @Override
    public void shutdownNow() {

    }

    @Override
    public ServiceID getServiceID() {
        return ServiceID.QUERY_ENGINE_SERVICE;
    }

    public static QueryEngine getInstance() {
        return QueryEngine.InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {

        private InstanceHolder() {}

        private static final QueryEngine INSTANCE = new QueryEngine();
    }
}
