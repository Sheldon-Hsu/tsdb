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

package com.tsdb.jdbc.common.config;

public class TSDBConfig {

    private String rpcAddress = "0.0.0.0";
    private int rpcPort = 9088;


    private int maxMemStoreNumber = 0;
    private long workMemSize = 0L;
    private int longestFlushTime = 0;

    /** How many threads can concurrently flush. When <= 0, use CPU core number. */
    private int concurrentFlushThread = Runtime.getRuntime().availableProcessors();
    private int flushQueueSize = 10;
    private int queryThreadSize = Runtime.getRuntime().availableProcessors();
    private int queryQueueSize = 10;
    private int writeThreadSize = Runtime.getRuntime().availableProcessors();
    private int writeQueueSize = 10;


    public String getRpcAddress() {
        return rpcAddress;
    }

    public void setRpcAddress(String rpcAddress) {
        this.rpcAddress = rpcAddress;
    }

    public int getRpcPort() {
        return rpcPort;
    }

    public void setRpcPort(int rpcPort) {
        this.rpcPort = rpcPort;
    }

    public int getLongestFlushTime() {
        return longestFlushTime;
    }

    public void setLongestFlushTime(int longestFlushTime) {
        this.longestFlushTime = longestFlushTime;
    }

    public long getWorkMemSize() {
        return workMemSize;
    }

    public void setWorkMemSize(long workMemSize) {
        this.workMemSize = workMemSize;
    }

    public int getMaxMemStoreNumber() {
        return maxMemStoreNumber;
    }

    public void setMaxMemStoreNumber(int maxMemStoreNumber) {
        this.maxMemStoreNumber = maxMemStoreNumber;
    }

    public int getConcurrentFlushThread() {
        return concurrentFlushThread;
    }

    public void setConcurrentFlushThread(int concurrentFlushThread) {
        this.concurrentFlushThread = concurrentFlushThread;
    }

    public int getFlushQueueSize() {
        return flushQueueSize;
    }

    public void setFlushQueueSize(int flushQueueSize) {
        this.flushQueueSize = flushQueueSize;
    }

    public int getQueryThreadSize() {
        return queryThreadSize;
    }

    public void setQueryThreadSize(int queryThreadSize) {
        this.queryThreadSize = queryThreadSize;
    }

    public int getWriteThreadSize() {
        return writeThreadSize;
    }

    public void setWriteThreadSize(int writeThreadSize) {
        this.writeThreadSize = writeThreadSize;
    }

    public int getQueryQueueSize() {
        return queryQueueSize;
    }

    public void setQueryQueueSize(int queryQueueSize) {
        this.queryQueueSize = queryQueueSize;
    }

    public int getWriteQueueSize() {
        return writeQueueSize;
    }

    public void setWriteQueueSize(int writeQueueSize) {
        this.writeQueueSize = writeQueueSize;
    }
}
