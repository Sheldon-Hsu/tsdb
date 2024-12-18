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

package com.tsdb.jdbc.rpc;

import org.apache.thrift.TConfiguration;

public class TConfigurationConst {
  // https://github.com/apache/thrift/blob/master/doc/specs/thrift-tconfiguration.md
  public static final TConfiguration defaultTConfiguration =
      new TConfiguration(
          RpcUtils.THRIFT_FRAME_MAX_SIZE + 4,
          RpcUtils.THRIFT_FRAME_MAX_SIZE,
          TConfiguration.DEFAULT_RECURSION_DEPTH);

  private TConfigurationConst() {}
}
