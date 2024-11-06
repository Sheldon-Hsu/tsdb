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

public class StoreTaskPoolManager extends AbstractPoolManager{
    private final TSDBConfig config = TSDBDescriptor.getInstance().getConfig();
    private static final Logger LOGGER = LoggerFactory.getLogger(StoreTaskPoolManager.class);

    private StoreTaskPoolManager() {
    }

    public static StoreTaskPoolManager getInstance() {
        return StoreTaskPoolManager.InstanceHolder.instance;
    }
    @Override
    public Logger getLogger() {
        return null;
    }

    @Override
    public void start() {
        if (pool == null) {
            int queueSize = config.getWriteQueueSize();
            int threadCnt = config.getWriteThreadSize();
            pool = ThreadPoolFactory.newFixedThreadPool(threadCnt, ThreadName.STORE_SERVICE.getName(),queueSize);
        }
        pool.submit(()-> LOGGER.info("store task manager started."));

    }

    @Override
    public String getName() {
        return null;
    }


    private static class InstanceHolder {

        private InstanceHolder() {
            // allowed to do nothing
        }

        private static StoreTaskPoolManager instance = new StoreTaskPoolManager();
    }
}
