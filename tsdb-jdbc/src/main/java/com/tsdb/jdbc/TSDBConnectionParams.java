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
package com.tsdb.jdbc;

import com.tsdb.jdbc.rpc.RpcUtils;

import java.time.ZoneId;

public class TSDBConnectionParams {
    private String host = Config.TSDB_DEFAULT_HOST;
    private int port = Config.TSDB_DEFAULT_PORT;
    private String jdbcUriString;
    private String databaseName = Config.DEFAULT_DATABASE_NAME;
    private String username = Config.DEFAULT_USER;
    private String password = Config.DEFAULT_PASSWORD;

    // The version number of the client which used for compatibility in the server
    private Constant.Version version = Config.DEFAULT_VERSION;

    private int thriftDefaultBufferSize = RpcUtils.THRIFT_DEFAULT_BUF_CAPACITY;
    private int thriftMaxFrameSize = RpcUtils.THRIFT_FRAME_MAX_SIZE;
    private int networkTimeout = Config.DEFAULT_CONNECTION_TIMEOUT_MS;

    private String timeZone = ZoneId.systemDefault().toString();

    private boolean useSSL = false;
    private String trustStore;
    private String trustStorePwd;

    public TSDBConnectionParams(String url) {
        this.jdbcUriString = url;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getJdbcUriString() {
        return jdbcUriString;
    }

    public void setJdbcUriString(String jdbcUriString) {
        this.jdbcUriString = jdbcUriString;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getThriftDefaultBufferSize() {
        return thriftDefaultBufferSize;
    }

    public void setThriftDefaultBufferSize(int thriftDefaultBufferSize) {
        this.thriftDefaultBufferSize = thriftDefaultBufferSize;
    }

    public int getThriftMaxFrameSize() {
        return thriftMaxFrameSize;
    }

    public void setThriftMaxFrameSize(int thriftMaxFrameSize) {
        this.thriftMaxFrameSize = thriftMaxFrameSize;
    }

    public Constant.Version getVersion() {
        return version;
    }

    public void setVersion(Constant.Version version) {
        this.version = version;
    }

    public void setNetworkTimeout(int networkTimeout) {
        if (networkTimeout < 0) {
            this.networkTimeout = Config.DEFAULT_CONNECTION_TIMEOUT_MS;
        } else {
            this.networkTimeout = networkTimeout;
        }
    }

    public int getNetworkTimeout() {
        return this.networkTimeout;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getTimeZone() {
        return this.timeZone;
    }

    public boolean isUseSSL() {
        return useSSL;
    }

    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }

    public String getTrustStore() {
        return trustStore;
    }

    public void setTrustStore(String trustStore) {
        this.trustStore = trustStore;
    }

    public String getTrustStorePwd() {
        return trustStorePwd;
    }

    public void setTrustStorePwd(String trustStorePwd) {
        this.trustStorePwd = trustStorePwd;
    }
}
