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
package com.tsdb.jdbc.server.concurrent.threadpool;

import com.tsdb.jdbc.common.config.TSDBConfig;
import com.tsdb.jdbc.common.config.TSDBDescriptor;

import com.tsdb.jdbc.server.concurrent.ThreadName;
import com.tsdb.jdbc.server.concurrent.ThreadPoolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlushTaskPoolManager extends AbstractPoolManager {
    private final TSDBConfig config = TSDBDescriptor.getInstance().getConfig();

    private static final Logger logger = LoggerFactory.getLogger(FlushTaskPoolManager.class);

    private FlushTaskPoolManager() {
    }

    public static FlushTaskPoolManager getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public String getName() {
        return "flush task";
    }

    @Override
    public void start() {
        if (pool == null) {
            int queueSize = config.getFlushQueueSize();
            int threadCnt = config.getConcurrentFlushThread();
            pool = ThreadPoolFactory.newFixedThreadPool(threadCnt, ThreadName.FLUSH_SERVICE.getName(),queueSize);
        }
        logger.info("Flush task manager started.");
    }

    @Override
    public void stop() {
        super.stop();
        logger.info("Flush task manager stopped");
    }

    private static class InstanceHolder {

        private InstanceHolder() {
            // allowed to do nothing
        }

        private static FlushTaskPoolManager instance = new FlushTaskPoolManager();
    }
}
