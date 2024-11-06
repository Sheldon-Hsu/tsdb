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

package com.tsdb.server.concurrent.threadpool;

import com.tsdb.jdbc.common.config.TSDBConfig;
import com.tsdb.jdbc.common.config.TSDBDescriptor;
import com.tsdb.server.concurrent.ThreadName;
import com.tsdb.server.concurrent.ThreadPoolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryTaskPoolManager extends AbstractPoolManager {
    private final TSDBConfig config = TSDBDescriptor.getInstance().getConfig();
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryTaskPoolManager.class);

    private QueryTaskPoolManager() {
    }

    public static QueryTaskPoolManager getInstance() {
        return QueryTaskPoolManager.InstanceHolder.instance;
    }
    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    public void start() {
        if (pool == null) {
            int queueSize = config.getQueryQueueSize();
            int threadCnt = config.getQueryThreadSize();
            pool = ThreadPoolFactory.newFixedThreadPool(threadCnt, ThreadName.QUERY_SERVICE.getName(),queueSize);
        }
        pool.submit(()-> LOGGER.info("query task manager started."));

    }

    @Override
    public String getName() {
        return null;
    }


    private static class InstanceHolder {

        private InstanceHolder() {
            // allowed to do nothing
        }

        private static QueryTaskPoolManager instance = new QueryTaskPoolManager();
    }
}
