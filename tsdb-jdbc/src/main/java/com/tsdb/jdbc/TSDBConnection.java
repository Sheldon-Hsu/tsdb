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


import com.tsdb.jdbc.rpc.RpcTransportFactory;
import com.tsdb.jdbc.rpc.RpcUtils;
import com.tsdb.jdbc.rpc.StatementExecutionException;
import com.tsdb.rpc.thrift.TSCloseSessionReq;
import com.tsdb.rpc.thrift.TSDBRpcService;
import com.tsdb.rpc.thrift.TSOpenSessionReq;
import com.tsdb.rpc.thrift.TSOpenSessionResp;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.ZoneId;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class TSDBConnection implements Connection {
    private static final Logger logger = LoggerFactory.getLogger(TSDBConnection.class);
    private TSDBConnectionParams params;
    private ZoneId zoneId;
    private boolean autoCommit;
    private String url;
    private String userName;

    private boolean isClosed = true;

    private long sessionId = -1;
    private TSDBRpcService.Iface client = null;
    private TTransport transport;

    public TSDBConnection(String user,
                          String database,
                          Properties info,
                          String url) throws SQLException, TTransportException {

        params = Utils.parseUrl(url, info);
        this.url = url;
        openTransport();
        setClient(RpcUtils.newSynchronizedClient(new TSDBRpcService.Client(new TBinaryProtocol(transport))));
        openSession();
        autoCommit = false;
    }

    @Override
    public Statement createStatement() throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return null;
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return null;
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return null;
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {

    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return false;
    }

    @Override
    public void commit() throws SQLException {

    }

    @Override
    public void rollback() throws SQLException {

    }

    @Override
    public void close() throws SQLException {
        if (isClosed) {
            return;
        }
        TSCloseSessionReq req = new TSCloseSessionReq(sessionId);
        try {
            getClient().closeSession(req);
        } catch (TException e) {
            throw new SQLException(
                    "Error occurs when closing session at server. Maybe server is down.", e);
        } finally {
            isClosed = true;
            if (transport != null) {
                transport.close();
            }
        }
    }

    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return null;
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {

    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return false;
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {

    }

    @Override
    public String getCatalog() throws SQLException {
        return null;
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {

    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return 0;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {

    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return null;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return null;
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return null;
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {

    }

    @Override
    public void setHoldability(int holdability) throws SQLException {

    }

    @Override
    public int getHoldability() throws SQLException {
        return 0;
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return null;
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return null;
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {

    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {

    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return null;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return null;
    }

    @Override
    public Clob createClob() throws SQLException {
        return null;
    }

    @Override
    public Blob createBlob() throws SQLException {
        return null;
    }

    @Override
    public NClob createNClob() throws SQLException {
        return null;
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return null;
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return false;
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {

    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {

    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return null;
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return null;
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return null;
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return null;
    }

    @Override
    public void setSchema(String schema) throws SQLException {

    }

    @Override
    public String getSchema() throws SQLException {
        return null;
    }

    @Override
    public void abort(Executor executor) throws SQLException {

    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {

    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return 10000;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    private void openTransport() throws SQLException, TTransportException {
        RpcTransportFactory.setDefaultBufferCapacity(params.getThriftDefaultBufferSize());
        RpcTransportFactory.setThriftMaxFrameSize(params.getThriftMaxFrameSize());

        if (params.isUseSSL()) {
            transport =
                    RpcTransportFactory.getInstance().getTransport(
                            params.getHost(),
                            params.getPort(),
                            getNetworkTimeout(),
                            params.getTrustStore(),
                            params.getTrustStorePwd());
        } else {
            transport =
                    RpcTransportFactory.getInstance().getTransport(
                            params.getHost(), params.getPort(), getNetworkTimeout());
        }
        if (!transport.isOpen()) {
            transport.open();
        }
    }

    public TSDBRpcService.Iface getClient() {
        return client;
    }

    public void setClient(TSDBRpcService.Iface client) {
        this.client = client;
    }

    private void openSession() throws SQLException {
        TSOpenSessionReq openRequest = new TSOpenSessionReq();
        openRequest.setUsername(params.getUsername());
        openRequest.setPassword(params.getPassword());
        openRequest.setDatabase(params.getDatabaseName());
        openRequest.setHost(params.getHost());
        openRequest.setZoneId(params.getTimeZone());
        TSOpenSessionResp openResp = null;
        try {
            openResp = client.openSession(openRequest);
            sessionId = openResp.getSessionId();
            RpcUtils.verifySuccess(openResp.getStatus());
        } catch (TException e) {
            transport.close();
            throw new SQLException(e);
        } catch (StatementExecutionException e) {
            transport.close();
            throw new TSDBSQLException(e.getMessage(), openResp.getStatus());
        }
        isClosed = false;
    }
}
