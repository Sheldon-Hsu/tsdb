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

package com.tsdb.server.flush;

import com.tsdb.server.concurrent.threadpool.FlushTaskPoolManager;
import com.tsdb.server.memory.IWMemStore;
import com.tsdb.server.service.IService;

public class FlushManager implements IService {
    private static final FlushTaskPoolManager taskPoolManager = FlushTaskPoolManager.getInstance();

    public void addToFlush(IWMemStore wMemStore){
        FlushMemStoreTask flushMemStoreTask = new FlushMemStoreTask(wMemStore);
        taskPoolManager.submit(flushMemStoreTask);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public void shutdownNow() {

    }

    private FlushManager() {}

    public static FlushManager getInstance() {
        return InstanceHolder.instance;
    }

    private static class InstanceHolder {

        private InstanceHolder() {}

        private static FlushManager instance = new FlushManager();
    }
}
