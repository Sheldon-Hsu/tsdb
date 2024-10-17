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

package com.tsdb.server.storage.region;

import com.tsdb.server.service.IService;
import com.tsdb.server.service.ServiceID;

public class RegionServer implements IService {

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void shutdownNow() {

    }

    @Override
    public ServiceID getServiceID() {
        return ServiceID.REGION_SERVICE;
    }

    private static class InstanceHolder {

        private static final RegionServer INSTANCE = new RegionServer();

        private InstanceHolder() {
        }
    }

    public static RegionServer getInstance() {
        return RegionServer.InstanceHolder.INSTANCE;
    }
}
