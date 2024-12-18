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

package com.tsdb.jdbc.tsfile.exception.compress;

/**
 * This exception will be thrown when the codec is not supported by tsfile, meaning there is no
 * matching type defined in CompressionCodecName.
 */
public class CompressionTypeNotSupportedException extends RuntimeException {

    private static final long serialVersionUID = -8659367946905193448L;
    private final Class<?> codecClass;

    public CompressionTypeNotSupportedException(String codecType) {
        super("codec not supported: " + codecType);
        this.codecClass = null;
    }

    public Class<?> getCodecClass() {
        return codecClass;
    }
}
