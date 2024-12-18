/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.tsdb.jdbc.rpc;

import com.tsdb.rpc.thrift.TSDBRpcService;
import org.apache.thrift.TException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SynchronizedHandler implements InvocationHandler {

  private final TSDBRpcService.Iface client;

  public SynchronizedHandler(TSDBRpcService.Iface client) {
    this.client = client;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {
      synchronized (client) {
        return method.invoke(client, args);
      }
    } catch (InvocationTargetException e) {
      // all IFace APIs throw TException
      if (e.getTargetException() instanceof TException) {
        throw e.getTargetException();
      } else {
        // should not happen
        throw new TException("Error in calling method " + method.getName(), e.getTargetException());
      }
    } catch (Exception e) {
      throw new TException("Error in calling method " + method.getName(), e);
    }
  }
}
