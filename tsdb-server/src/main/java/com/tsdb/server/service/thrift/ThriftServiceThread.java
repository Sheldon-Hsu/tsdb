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

import com.tsdb.jdbc.common.config.TSDBConfig;
import com.tsdb.jdbc.common.config.TSDBDescriptor;
import com.tsdb.jdbc.rpc.RpcTransportFactory;
import com.tsdb.jdbc.rpc.TSStatusCode;
import com.tsdb.rpc.thrift.TSDBRpcService;
import com.tsdb.server.concurrent.ThreadName;
import com.tsdb.server.concurrent.ThreadPoolFactory;
import com.tsdb.server.exception.service.StartupException;
import com.tsdb.server.service.thrift.handler.RPCServiceHandler;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThriftServiceThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(RPCService.class);
    private TSDBServiceImpl impl;
    private TSDBRpcService.Processor<TSDBServiceImpl> processor;
    private TServerTransport serverTransport;
    private TServer tServer;

    private static TBinaryProtocol.Factory binaryProtocolFactory = new TBinaryProtocol.Factory();
    private static TSDBConfig config = TSDBDescriptor.getInstance().getConfig();

    private String threadName = ThreadName.RPC_SERVICE.getName();


    public ThriftServiceThread() throws StartupException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        int port = config.getRpcPort();
        String host = config.getRpcAddress();
        impl = ( TSDBServiceImpl)Class.forName(TSDBServiceImpl.class.getName()).newInstance();
        processor = new TSDBRpcService.Processor<>(impl);
        try {
            serverTransport = new TServerSocket(new InetSocketAddress("0.0.0.0",port));
            TThreadPoolServer.Args poolArgs = initSyncedPoolArgs(
                    serverTransport,
                    processor,
                    threadName,
                    config.getRpcMaxConcurrentClientNum(),
                    config.getThriftServerAwaitTimeForStopService());
            tServer = new TThreadPoolServer(poolArgs);
            tServer.setServerEventHandler(new RPCServiceHandler(impl));
        } catch (TTransportException e) {
            String errorMessage = String.format("Bind for %s:%s failed: port is already allocated.", host, port);
            logger.error(errorMessage);
            throw new StartupException(errorMessage, TSStatusCode.START_UP_ERROR.getStatusCode());
        }

    }


    private TThreadPoolServer.Args initSyncedPoolArgs(
            TServerTransport transport,
            TProcessor processor,
            String threadsName,
            int maxWorkerThreads,
            int timeoutSecond) {
        int min = Math.min(maxWorkerThreads, Runtime.getRuntime().availableProcessors());
        TThreadPoolServer.Args poolArgs = new TThreadPoolServer.Args(transport);
        SynchronousQueue<Runnable> queue = new SynchronousQueue<>();
        poolArgs.executorService = new ThreadPoolExecutor(min, maxWorkerThreads,60, TimeUnit.SECONDS,queue);
        poolArgs.processor(processor);
        poolArgs.protocolFactory(binaryProtocolFactory);
        poolArgs.transportFactory(RpcTransportFactory.getInstance());
        poolArgs.maxWorkerThreads(maxWorkerThreads).minWorkerThreads(min).stopTimeoutVal(timeoutSecond);
        return poolArgs;
    }

    @Override
    public void run() {
        super.run();
        tServer.serve();
    }
}
