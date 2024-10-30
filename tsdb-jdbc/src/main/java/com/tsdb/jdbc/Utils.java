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

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    static final Pattern SUFFIX_URL_PATTERN = Pattern.compile("([0-9]{1,5})(/|\\\\?.*=.*(&.*=.*)*)?");
    static final String COLON = ":";
    static final String SLASH = "/";
    static final String PARAMETER_SEPARATOR = "?";

    static final String RPC_COMPRESS = "rpc_compress";

    static TSDBConnectionParams parseUrl(String url, Properties info) throws TSDBURLException {
        TSDBConnectionParams params = new TSDBConnectionParams(url);


        boolean isUrlLegal = false;
        Matcher matcher = null;
        String host = null;
        String suffixURL = null;
        if (url.startsWith(Config.TSDB_URL_PREFIX)) {
            String subURL = url.substring(Config.TSDB_URL_PREFIX.length());
            int i = subURL.lastIndexOf(COLON);
            host = subURL.substring(0, i);
            suffixURL = subURL.substring(i + 1);

            matcher = SUFFIX_URL_PATTERN.matcher(suffixURL);
            if (matcher.matches() && parseUrlParam(subURL, info)) {
                isUrlLegal = true;
            }
        }
        if (!isUrlLegal) {
            throw new TSDBURLException(
                    "Error url format, url should be jdbc:tsdb://anything:port/ or jdbc:tsdb://anything:port?property1=value1&property2=value2");
        }

        params.setHost(host);

        // parse port
        String port = suffixURL;
        if (suffixURL.contains(PARAMETER_SEPARATOR)) {
            port = suffixURL.split("\\" + PARAMETER_SEPARATOR)[0];
        } else if (suffixURL.contains(SLASH)) {
            port = suffixURL.substring(0, suffixURL.length() - 1);
        }
        params.setPort(Integer.parseInt(port));

        if (info.containsKey(Config.AUTH_USER)) {
            params.setUsername(info.getProperty(Config.AUTH_USER));
        }
        if (info.containsKey(Config.AUTH_PASSWORD)) {
            params.setPassword(info.getProperty(Config.AUTH_PASSWORD));
        }
        if (info.containsKey(Config.DEFAULT_BUFFER_CAPACITY)) {
            params.setThriftDefaultBufferSize(
                    Integer.parseInt(info.getProperty(Config.DEFAULT_BUFFER_CAPACITY)));
        }
        if (info.containsKey(Config.THRIFT_FRAME_MAX_SIZE)) {
            params.setThriftMaxFrameSize(
                    Integer.parseInt(info.getProperty(Config.THRIFT_FRAME_MAX_SIZE)));
        }
        if (info.containsKey(Config.VERSION)) {
            params.setVersion(Constant.Version.valueOf(info.getProperty(Config.VERSION)));
        }
        if (info.containsKey(Config.NETWORK_TIMEOUT)) {
            params.setNetworkTimeout(Integer.parseInt(info.getProperty(Config.NETWORK_TIMEOUT)));
        }
        if (info.containsKey(Config.TIME_ZONE)) {
            params.setTimeZone(info.getProperty(Config.TIME_ZONE));
        }

        if (info.containsKey(Config.USE_SSL)) {
            params.setUseSSL(Boolean.parseBoolean(info.getProperty(Config.USE_SSL)));
        }

        if (info.containsKey(Config.TRUST_STORE)) {
            params.setTrustStore(info.getProperty(Config.TRUST_STORE));
        }

        if (info.containsKey(Config.TRUST_STORE_PWD)) {
            params.setTrustStorePwd(info.getProperty(Config.TRUST_STORE_PWD));
        }

        return params;
    }


    private static boolean parseUrlParam(String subURL, Properties info) {
        if (!subURL.contains("?")) {
            return true;
        }
        String paramURL = subURL.substring(subURL.indexOf('?') + 1);
        String[] params = paramURL.split("&");
        for (String tmpParam : params) {
            String[] paramSplit = tmpParam.split("=");
            if (paramSplit.length != 2) {
                return false;
            }
            String key = tmpParam.split("=")[0];
            String value = tmpParam.split("=")[1];
            switch (key) {
                case RPC_COMPRESS:
                    if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
                        Config.rpcThriftCompressionEnable = Boolean.parseBoolean(value);
                    } else {
                        return false;
                    }
                    break;
                case Config.USE_SSL:
                case Config.TRUST_STORE:
                case Config.TRUST_STORE_PWD:
                case Config.VERSION:
                case Config.NETWORK_TIMEOUT:
                    info.put(key, value);
                    break;
                case Config.TIME_ZONE:
                    try {
                        // Check the validity of the time zone string.
                        ZoneId.of(value);
                    } catch (DateTimeException e) {
                        return false;
                    }
                    info.put(key, value);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
