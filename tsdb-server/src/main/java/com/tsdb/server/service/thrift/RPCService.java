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

package com.tsdb.server.service.thrift;

import com.tsdb.common.config.TSDBConfig;
import com.tsdb.common.config.TSDBConstant;
import com.tsdb.common.config.TSDBDescriptor;
import com.tsdb.rpc.TSStatusCode;
import com.tsdb.rpc.thrift.TSDBRpcService;
import com.tsdb.server.exception.service.StartupException;
import com.tsdb.server.service.IService;
import com.tsdb.server.service.ServiceID;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RPCService implements IService {
    private static final Logger logger = LoggerFactory.getLogger(RPCService.class);
    private static final String STATUS_UP = "UP";
    private static final String STATUS_DOWN = "DOWN";

    private RPCServiceThread rpcServiceThread;


    private TProcessor processor;

    @Override
    public void start() throws StartupException {
        if (STATUS_UP.equals(getRPCServiceStatus())) {
            logger.info("{}: has en already running.", TSDBConstant.GLOBAL_DB_NAME);
            return;
        }
        rpcServiceThread = new RPCServiceThread();
        logger.info("RPCService start...");
        rpcServiceThread.start();

    }

    @Override
    public void stop() {

    }

    @Override
    public void shutdownNow() {

    }


    private void startService() {

    }

    private void initProcessor() {

    }

    private String getRPCServiceStatus() {
        if (rpcServiceThread == null){
            return STATUS_DOWN;
        }else {
            return STATUS_UP;
        }

    }

    @Override
    public ServiceID getServiceID() {
        return ServiceID.RPC_SERVICE;
    }

    public static RPCService getInstance() {
        return RPCService.InstanceHolder.INSTANCE;
    }


    class RPCServiceThread extends Thread {
        private TSDBRpcService.Processor<TSDBServiceImpl> processor;
        private TServerTransport serverTransport;
        private TServer tServer;
        TSDBConfig config = TSDBDescriptor.getInstance().getConfig();


        public RPCServiceThread() throws StartupException {
            int port = config.getRpcPort();
            String host = config.getRpcAddress();
            processor = new TSDBRpcService.Processor<>(new TSDBServiceImpl());
            try {
                serverTransport = new TServerSocket(port);
            } catch (TTransportException e) {
                String errorMessage = String.format("Bind for %s:%s failed: port is already allocated.",host,port);
                logger.error(errorMessage);
                throw new StartupException(errorMessage, TSStatusCode.START_UP_ERROR.getStatusCode());
            }
            tServer = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
        }

        @Override
        public void start(){
            super.start();
            tServer.serve();
        }
    }


    private static class InstanceHolder {

        private InstanceHolder() {
        }

        private static final RPCService INSTANCE = new RPCService();
    }

}
