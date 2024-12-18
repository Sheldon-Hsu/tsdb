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

include "common.thrift"
include "client.thrift"
namespace java com.tsdb.rpc.thrift


service TSDBRpcService{
    client.TSOpenSessionResp openSession(1:client.TSOpenSessionReq request);
    common.TSDBStatus closeSession(1:client.TSCloseSessionReq request);
    common.TSDBStatus insertData(1:client.InsertDataReq request);
}


