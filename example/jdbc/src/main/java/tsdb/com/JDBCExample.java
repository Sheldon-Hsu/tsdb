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

package tsdb.com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCExample {
    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCExample.class);

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.tsdb.jdbc.Driver");
        Connection connection =
                DriverManager.getConnection(
                        "jdbc:tsdb://127.0.0.1:9088?version=V_0_1", "root", "root");
        LOGGER.info("connection success.");
        connection.close();
        LOGGER.info("connection close success.");

    }
}
