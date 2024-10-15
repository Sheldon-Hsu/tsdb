/**
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

namespace java com.tsdb.rpc.thrift

struct TSDBStatus{
    1: required i32 code
    2: required string message
}

exception TSDBException{
    1: i32 code,
    2: string msg,
    3: list<TSDBException> subException;
}



struct TSOpenSessionResp {
  1: required TSDBStatus status
  // Session id
  2: optional i64 sessionId
  // The configuration settings for this session.
  4: optional map<string, string> configuration
}



service TSDBRpcService{
    TSOpenSessionResp openSession(1:TSOpenSessionReq request);
    TSDBStatus closeSession(1:TSCloseSessionReq request);
    TSDBStatus insertData(1:InsertDataReq request);

}


