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

import com.tsdb.rpc.thrift.TSDBRpcService;
import com.tsdb.rpc.thrift.TSOpenSessionReq;
import com.tsdb.rpc.thrift.TSOpenSessionResp;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TSDBServiceImplTest {

    private static final String localhost = "127.0.0.1";
    private static final int port = 9000;

    @BeforeAll
    void setUp() throws IOException, TTransportException {
        ServerSocket socket = new ServerSocket(port);
        TServerSocket tServerSocket = new TServerSocket(socket);
        TSDBRpcService.Processor<TSDBRpcService.Iface> processor =
                new TSDBRpcService.Processor<>(new TSDBServiceImpl());
        TSimpleServer.Args args = new TServer.Args(tServerSocket);
        args.processor(processor);
        args.protocolFactory(new TBinaryProtocol.Factory());

        TSimpleServer server = new TSimpleServer(args);
        System.out.println("server started");

        Runnable runnable = server::serve;
        Thread thread = new Thread(runnable);
        thread.start();


    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void openSession() throws TException {
        System.out.println("start open session");
        TTransport tTransport = null;
        tTransport = new TSocket(localhost, port, 1000);
        TProtocol protocol = new TBinaryProtocol(tTransport);
        TSDBRpcService.Client client = new TSDBRpcService.Client(protocol);
        tTransport.open();
        TSOpenSessionReq req = new TSOpenSessionReq();
        req.setDatabase("test");
        req.setUsername("test");
        req.setPassword("test");
        req.setHost(localhost);
        req.setZoneId("UTC+8");
        TSOpenSessionResp resp = client.openSession(req);
        Assertions.assertEquals(1,resp.sessionId);
    }

    @Test
    void closeSession() {
    }

    @Test
    void insertData() {
    }
}