package com.tsdb.tsfile.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;

public class TSFileDescriptor {

    private static final Logger logger = LoggerFactory.getLogger(TSFileDescriptor.class);
    private final TSFileConfig conf = new TSFileConfig();

    TSFileDescriptor() {
        init();
    }

    public static TSFileDescriptor getInstance() {
        return TsfileDescriptorHolder.INSTANCE;
    }

    private void init() {
        loadProperties().ifPresent(this::overwriteConfigByCustomSettings);
    }

    public void overwriteConfigByCustomSettings(Properties properties) {
        PropertiesOverWriter writer = new PropertiesOverWriter(properties);

    }


    private static class PropertiesOverWriter {

        private final Properties properties;

        public PropertiesOverWriter(Properties properties) {
            if (properties == null) {
                throw new NullPointerException("properties cannot be null");
            }
            this.properties = properties;
        }

        public void setInt(Consumer<Integer> setter, String propertyKey) {
            set(setter, propertyKey, Integer::parseInt);
        }

        public void setDouble(Consumer<Double> setter, String propertyKey) {
            set(setter, propertyKey, Double::parseDouble);
        }

        public void setString(Consumer<String> setter, String propertyKey) {
            set(setter, propertyKey, Function.identity());
        }

        private <T> void set(Consumer<T> setter, String propertyKey, Function<String, T> propertyValueConverter) {
            String value = this.properties.getProperty(propertyKey);

            if (value != null) {
                try {
                    T v = propertyValueConverter.apply(value);
                    setter.accept(v);
                } catch (Exception e) {
                    logger.warn("invalid value for {}, use the default value", propertyKey);
                }
            }
        }
    }

    private Optional<Properties> loadProperties() {

        return Optional.empty();
    }


    public TSFileConfig getConf() {
        return conf;
    }

    private static class TsfileDescriptorHolder {

        private TsfileDescriptorHolder() {
            throw new IllegalAccessError("Utility class");
        }

        private static final TSFileDescriptor INSTANCE = new TSFileDescriptor();
    }
}
