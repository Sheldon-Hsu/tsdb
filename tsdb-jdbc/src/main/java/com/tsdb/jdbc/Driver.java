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

package com.tsdb.jdbc;

import com.tsdb.jdbc.util.TSQLException;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.sql.*;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

public class Driver implements java.sql.Driver {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Driver.class);
    private static Driver registeredDriver;

    static {
        try {
            register();
        } catch (SQLException e) {
            throw new ExceptionInInitializerError(e);
        }
    }


    private Properties defaultProperties;

    private synchronized Properties getDefaultProperties() throws IOException {
        if (defaultProperties != null) {
            return defaultProperties;
        }

        // Make sure we load properties with the maximum possible privileges.
        try {
            defaultProperties =
                    AccessController.doPrivileged((PrivilegedExceptionAction<Properties>) () -> loadDefaultProperties());
        } catch (PrivilegedActionException e) {
            throw (IOException) e.getException();
        }

        return defaultProperties;
    }


    private Properties loadDefaultProperties() throws IOException {
        Properties merged = new Properties();

        return merged;
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        Properties defaults;
        if (!url.startsWith("jdbc:tsdb:")) {
            return null;
        }
        try {
            defaults = getDefaultProperties();
        } catch (IOException e) {
            throw new TSQLException(String.format("Error loading default settings from driverconfig.properties: %s", e));
        }
        Properties props = new Properties(defaults);
        if (info != null) {
            Set<String> e = info.stringPropertyNames();
            for (String propName : e) {
                String propValue = info.getProperty(propName);
                if (propValue == null) {
                    throw new TSQLException(
                            String.format("Properties for the driver contains a non-string value for the key: %s ", propName));
                }
                props.setProperty(propName, propValue);
            }
        }
        logger.info("Connecting with URL:{}", url);

        long timeout = timeout(props);
        if (timeout <= 0) {
            try {
                return makeConnection(url, props);
            } catch (TTransportException e) {
                logger.error("create connection error:",e);
                throw new SQLException(e);
            }
        }
        ConnectThread ct = new ConnectThread(url, props);
        Thread thread = new Thread(ct, "TSDB JDBC driver connection thread");
        thread.setDaemon(true); // Don't prevent the VM from shutting down
        thread.start();
        return ct.getResult(timeout);
    }

    private static class ConnectThread implements Runnable {
        ConnectThread(String url, Properties props) {
            this.url = url;
            this.props = props;
        }

        public void run() {
            Connection conn;
            Throwable error;

            try {
                conn = makeConnection(url, props);
                error = null;
            } catch (Throwable t) {
                conn = null;
                error = t;
            }

            synchronized (this) {
                if (abandoned) {
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (SQLException e) {
                        }
                    }
                } else {
                    result = conn;
                    resultException = error;
                    notify();
                }
            }
        }


        public Connection getResult(long timeout) throws SQLException {
            long expiry = System.currentTimeMillis() + timeout;
            synchronized (this) {
                while (true) {
                    if (result != null) {
                        return result;
                    }

                    if (resultException != null) {
                        if (resultException instanceof SQLException) {
                            resultException.fillInStackTrace();
                            throw (SQLException) resultException;
                        } else {
                            throw new TSQLException(
                                    String.format(
                                            "Something unusual has occurred to cause the driver to fail. Please report this exception.%s"
                                            , resultException));
                        }
                    }

                    long delay = expiry - System.currentTimeMillis();
                    if (delay <= 0) {
                        abandoned = true;
                        throw new TSQLException(String.format("Connection attempt timed out."));
                    }

                    try {
                        wait(delay);
                    } catch (InterruptedException ie) {

                        // reset the interrupt flag
                        Thread.currentThread().interrupt();
                        abandoned = true;

                        // throw an unchecked exception which will hopefully not be ignored by the calling code
                        throw new RuntimeException(String.format("Interrupted while attempting to connect."));
                    }
                }
            }
        }

        private final String url;
        private final Properties props;
        private Connection result;
        private Throwable resultException;
        private boolean abandoned;
    }

    private static Connection makeConnection(String url, Properties props) throws SQLException, TTransportException {
        return new TSDBConnection(user(props), database(props), props, url);
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return false;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    private static String user(Properties props) {
        return props.getProperty("user", "");
    }

    private static String database(Properties props) {
        return props.getProperty("TSDBNAME", "");
    }

    private static long timeout(Properties props) {
        String timeout = "0";
        if (timeout != null) {
            try {
                return (long) (Float.parseFloat(timeout) * 1000);
            } catch (NumberFormatException e) {
                logger.warn("Couldn't parse loginTimeout value: {}", timeout);
            }
        }
        return (long) DriverManager.getLoginTimeout() * 1000;
    }

    public static void register() throws SQLException {
        if (isRegistered()) {
            throw new IllegalStateException(
                    "Driver is already registered. It can only be registered once.");
        }
        Driver registeredDriver = new Driver();
        DriverManager.registerDriver(registeredDriver);
        Driver.registeredDriver = registeredDriver;
    }

    public static boolean isRegistered() {
        return registeredDriver != null;
    }

}
