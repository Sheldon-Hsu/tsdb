/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.tsdb.jdbc;



import com.tsdb.common.rpc.thrift.TSDBStatus;

import java.sql.SQLException;

public class TSDBSQLException extends SQLException {

  private static final long serialVersionUID = -3306001287342258977L;

  public TSDBSQLException(String reason) {
    super(reason);
  }

  public TSDBSQLException(String reason, TSDBStatus status) {
    super(reason, status.message, status.code);
  }

  public TSDBSQLException(Throwable cause) {
    super(cause);
  }
}
