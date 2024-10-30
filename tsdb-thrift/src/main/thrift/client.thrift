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
namespace java com.tsdb.rpc.thrift


struct TSOpenSessionResp {
  1: required common.TSDBStatus status
  // Session id
  2: optional i64 sessionId
  // The configuration settings for this session.
  4: optional map<string, string> configuration
}

struct TSOpenSessionReq {
  1: required string zoneId
  2: required string database
  3: required string username
  4: optional string password
  6: required string host
  5: optional map<string, string> configuration
}

struct TSCloseSessionReq {
  1: required i64 sessionId
}

struct InsertDataReq{
    1: required i64 sessionId
    2: required string catalog
    3: required string table
    4: required binary data
}


struct TSFetchResultsResp{
  1: required common.TSDBStatus status
  2: required bool hasResultSet
  3: required bool isAlign
  4: optional TSQueryDataSet queryDataSet
  5: optional list<binary> queryResult
  6: optional bool moreData
}


struct TSExecuteStatementResp {
  1: required common.TSDBStatus status
  2: optional i64 queryId
  // Column names in select statement of SQL
  3: optional list<string> columns
  4: optional string operationType
  5: optional bool ignoreTimeStamp
  // Data type list of columns in select statement of SQL
  6: optional list<string> dataTypeList
  7: optional TSQueryDataSet queryDataSet
  // for disable align statements, queryDataSet is null and nonAlignQueryDataSet is not null

  8: optional map<string, i32> columnNameIndexMap
  9: optional list<string> sgColumns
  10: optional list<byte> aliasColumns
  11: optional list<binary> queryResult
  12: optional bool moreData
}


struct TSQueryDataSet{
  // ByteBuffer for time column
  1: required binary time
  // ByteBuffer for each column values
  2: required list<binary> valueList
  // Bitmap for each column to indicate whether it is a null value
  3: required list<binary> bitmapList
}

