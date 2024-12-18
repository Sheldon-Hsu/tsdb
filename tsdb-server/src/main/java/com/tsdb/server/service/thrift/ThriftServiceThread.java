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
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.SynchronousQueue;

public class ThriftServiceThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(RPCService.class);
    private TSDBRpcService.Processor<TSDBServiceImpl> processor;
    private TServerTransport serverTransport;
    private TServer tServer;

    private static TBinaryProtocol.Factory binaryProtocolFactory = new TBinaryProtocol.Factory();
    private static TSDBConfig config = TSDBDescriptor.getInstance().getConfig();

    private String threadName = ThreadName.RPC_SERVICE.getName();


    public ThriftServiceThread() throws StartupException {
        int port = config.getRpcPort();
        String host = config.getRpcAddress();
        processor = new TSDBRpcService.Processor<>(new TSDBServiceImpl());
        try {
            serverTransport = new TServerSocket(port);
            TThreadPoolServer.Args poolArgs = initSyncedPoolArgs(
                    serverTransport,
                    processor,
                    threadName,
                    config.getRpcMaxConcurrentClientNum(),
                    config.getThriftServerAwaitTimeForStopService());
            tServer = new TThreadPoolServer(poolArgs);
        } catch (TTransportException e) {
            String errorMessage = String.format("Bind for %s:%s failed: port is already allocated.", host, port);
            logger.error(errorMessage);
            throw new StartupException(errorMessage, TSStatusCode.START_UP_ERROR.getStatusCode());
        }
        tServer = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
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
        poolArgs.executorService = ThreadPoolFactory.newFixedThreadPool(maxWorkerThreads, threadsName,queue);
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
