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

package com.tsdb.server.memory;

import com.tsdb.server.plan.physics.InsertRowPlan;
import com.tsdb.server.plan.physics.InsertRowsPlan;

public class WMemStore implements IWMemStore{
    @Override
    public void insert(InsertRowPlan insertRowPlan) {

    }

    @Override
    public void insert(InsertRowsPlan insertRowsPlan) {

    }

    @Override
    public long getMemSize() {
        return 0;
    }
}
