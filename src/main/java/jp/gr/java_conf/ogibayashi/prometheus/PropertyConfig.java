package jp.gr.java_conf.ogibayashi.prometheus;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyConfig {
    private static final Logger LOG = LoggerFactory.getLogger(ConsumerThread.class);

    public enum Constants {
        EXPORTER_PORT("exporter.port"),
        EXPORTER_METRIC_EXPIRE("exporter.metric.expire.seconds"),
        KAFKA_CONSUMER_TOPICS("kafka.consumer.topics"),
        KAKFA_CONSUMER_REMOVEPREFIX("kafka.consumer.remove.prefix");
        
        public final String key;

        Constants(String key) {
            this.key = key;
        }
    }

    private final Properties props;

    public PropertyConfig() {
        props = new Properties();
    }
    
    public PropertyConfig(String propFilePath) throws IOException {
        props = loadProperties(propFilePath);
        props.put("enable.auto.commit", "false");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    }

    public Pattern getTopicsPattern() {
        return Pattern.compile(props.getProperty(Constants.KAFKA_CONSUMER_TOPICS.key));
    }
    
    public Properties getProperties() {
        return props;
    }

    public long getMetricExpire() {
        return (long)Long.parseLong(get(Constants.EXPORTER_METRIC_EXPIRE.key, "0"));
    }

    public int getExporterPort() {
        return(getInt(PropertyConfig.Constants.EXPORTER_PORT.key, 9185));
    }
    
    public String get(String key) {
        String value = props.getProperty(key);
        if (value == null)
            throw new RuntimeException(key + " parameter not found in the configuration");
        return value;
    }

    public String get(String key, String defaultValue) {
        String value = props.getProperty(key);
        if (value == null)
            return defaultValue;
        return value;
    }

    public void set(String key, String value) {
        props.setProperty(key, value);
    }
    
    public int getInt(String key) {
        return (int)Long.parseLong(get(key));
    }

    public int getInt(String key, int defaultValue) {
        String value = props.getProperty(key);
        if (value == null)
            return defaultValue;
        return (int)Long.parseLong(get(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.valueOf(get(key)).booleanValue();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = props.getProperty(key);
        if (value == null)
            return defaultValue;
        return Boolean.valueOf(value).booleanValue();
    }


    private Properties loadProperties(String propFilePath) throws IOException {
        Properties props = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream(propFilePath);
            if (input != null) {
                props.load(input);
            } else {
                throw new FileNotFoundException(propFilePath + "' not found");
            }
        } finally {
            input.close();
        }

        return props;
    }

}
