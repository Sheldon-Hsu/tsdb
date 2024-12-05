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

package com.tsdb.jdbc.common.data;

import com.tsdb.jdbc.common.io.BytesUtils;
import com.tsdb.jdbc.common.io.IOUtils;

import java.sql.Types;

public enum DataType {
    BOOLEAN(Types.BOOLEAN),
    SMALLINT(Types.SMALLINT),
    INTEGER(Types.INTEGER),
    FLOAT(Types.FLOAT),
    BIGINT(Types.BIGINT),
    DOUBLE(Types.DOUBLE),
    VARCHAR(Types.VARCHAR),
    ARRAY(Types.ARRAY),
    BINARY(Types.BINARY),
    DATE(Types.DATE),
    TIMESTAMP(Types.TIMESTAMP)
    ;

    private final Integer code;

    DataType(Integer code) {
        this.code = code;
    }

    public int getFixLength() {
        switch (this) {
            case BOOLEAN:
                return 1;
            case SMALLINT:
                return 2;
            case INTEGER:
            case FLOAT:
                return 4;
            case BIGINT:
            case DOUBLE:
            case TIMESTAMP:
            case DATE:
                return 8;
            case VARCHAR:
            case ARRAY:
            case BINARY:
            default:
                return 0;
        }
    }

    public Integer getCode() {
        return code;
    }
    /**
     * get type byte.
     *
     * @return byte number
     */
    public byte[] serialize() {
        return BytesUtils.intToBytes(code);
    }
}
