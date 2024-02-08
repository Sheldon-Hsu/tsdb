package com.tsdb.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class TSDBDescriptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(TSDBDescriptor.class);

    private final TSDBConfig conf = new TSDBConfig();


    protected TSDBDescriptor() {
        loadProps();
    }

    private void loadProps() {
        Properties commonProperties = new Properties();
    }

    public void loadProperties(Properties properties) {

    }

    public static TSDBDescriptor getInstance() {
        return TSDBDescriptorHolder.INSTANCE;
    }

    public TSDBConfig getConfig() {
        return conf;
    }

    private static class TSDBDescriptorHolder {

        private static final TSDBDescriptor INSTANCE = new TSDBDescriptor();

        private TSDBDescriptorHolder() {
        }
    }

}
