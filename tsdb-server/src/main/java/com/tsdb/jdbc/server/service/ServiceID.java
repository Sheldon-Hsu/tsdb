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

package com.tsdb.jdbc.server.service;



public enum ServiceID {
  STORAGE_ENGINE_SERVICE("Storage Engine ServerService"),
  QUERY_ENGINE_SERVICE("Query Engine ServerService"),
  RPC_SERVICE("RPC ServerService"),
  FLUSH_SERVICE("Flush ServerService"),
  REGION_SERVICE("Region ServerService")
  ;

  private final String name;


  ServiceID(String name) {
    this.name = name;

  }

  public String getName() {
    return name;
  }



  private static String generateJmxName(String packageName, String jmxName) {
    return String.format("%s:type=%s", packageName, jmxName);
  }
}
