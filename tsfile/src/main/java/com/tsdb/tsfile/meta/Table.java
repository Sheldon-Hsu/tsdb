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

package com.tsdb.tsfile.meta;

import com.tsdb.common.data.DataType;
import com.tsdb.tsfile.compress.CompressionType;
import com.tsdb.tsfile.encoding.TSEncoding;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Table implements Serializable {
    private static final long serialVersionUID = 2103464296109291362L;
    private DataType[] dataTypes ;
    private CompressionType compressionType;
    public DataType[] getDataTypesOrdered(){
        return dataTypes;
    }

    private Map<DataType, TSEncoding> encoder = new HashMap<>();


    public void setEncoder(Map<DataType, TSEncoding> encoder) {
        this.encoder = encoder;
    }

    public Map<DataType, TSEncoding> getEncoders() {
        return encoder;
    }

    public void setDataTypes(DataType[] dataTypes) {
        this.dataTypes = dataTypes;
    }

    public CompressionType getCompressionType() {
        return compressionType;
    }

    public void setCompressionType(CompressionType compressionType) {
        this.compressionType = compressionType;
    }
}
