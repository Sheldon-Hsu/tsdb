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

package com.tsdb.server.service;

public interface IService {
    void start() ;

    /**
     * Stop accepting tasks. The tasks in the queue are closed after completion
     */
    void stop();

    /**
     * Stop accepting tasks, empty the tasks in the queue, and close the running tasks when they are completed
     */
    void shutdownNow();

    ServiceID getServiceID();
}
