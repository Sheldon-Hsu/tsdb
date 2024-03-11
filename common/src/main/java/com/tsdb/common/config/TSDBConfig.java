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

package com.tsdb.common.config;

public class TSDBConfig {

    private int maxMemStoreNumber = 0;
    private long workMemSize = 0L;
    private int longestFlushTime = 0;


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
}
