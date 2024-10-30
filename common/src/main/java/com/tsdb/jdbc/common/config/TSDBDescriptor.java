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

package com.tsdb.jdbc.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.ResourceBundle;

public class TSDBDescriptor {
    private static final Logger logger = LoggerFactory.getLogger(TSDBDescriptor.class);

    private final TSDBConfig conf = new TSDBConfig();


    protected TSDBDescriptor() {
        loadProps();
    }

    private void loadProps() {
        Properties commonProperties = new Properties();
    }

    public void loadProperties(Properties properties) {
        ResourceBundle resource = ResourceBundle.getBundle("tsdb.properties");
        conf.setRpcAddress(resource.getString("tsdb.host"));
        conf.setRpcPort(Integer.parseInt(resource.getString("tsdb.port")));
    }






    public static TSDBDescriptor getInstance() {
        return TSDBDescriptorHolder.INSTANCE;
    }

    public TSDBConfig getConfig() {
        return conf;
    }

    private static class TSDBDescriptorHolder {

        private static final TSDBDescriptor INSTANCE = new TSDBDescriptor();

        private TSDBDescriptorHolder() {
        }
    }

}
