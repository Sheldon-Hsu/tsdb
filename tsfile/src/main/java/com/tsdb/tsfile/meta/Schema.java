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
import com.tsdb.tsfile.encoding.encode.Encoder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Schema implements Serializable {
    private static final long serialVersionUID = 2103464296109291362L;

    private Map<DataType, Encoder> encoder = new HashMap<>();


    public void setEncoder(Map<DataType, Encoder> encoder) {
        this.encoder = encoder;
    }

    public Map<DataType, Encoder> getEncoders() {
        return encoder;
    }
}
