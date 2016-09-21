package jp.gr.java_conf.ogibayashi.prometheus;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class KafkaExporterLogEntryTest extends TestCase {

    private ObjectMapper mapper = new ObjectMapper();

    public void testEquals() throws IOException {
        final String logRecord1 = "{\"name\":\"foo\", \"labels\": { \"label1\": \"v1\", \"lable2\": \"v2\" }, \"value\": 9}";
        KafkaExporterLogEntry jsonRecord1 = mapper.readValue(logRecord1, KafkaExporterLogEntry.class);
        final String logRecord2 = "{\"name\":\"foo\", \"labels\": { \"label1\": \"v1\", \"lable2\": \"v2\" }, \"value\": 9}";
        KafkaExporterLogEntry jsonRecord2 = mapper.readValue(logRecord2, KafkaExporterLogEntry.class);

        assertEquals(jsonRecord1, jsonRecord2);
    }

    public void testHashCode() throws IOException {
        final String logRecord1 = "{\"name\":\"foo\", \"labels\": { \"label1\": \"v1\", \"lable2\": \"v2\" }, \"value\": 9}";
        KafkaExporterLogEntry jsonRecord1 = mapper.readValue(logRecord1, KafkaExporterLogEntry.class);
        final String logRecord2 = "{\"name\":\"foo\", \"labels\": { \"label1\": \"v1\", \"lable2\": \"v2\" }, \"value\": 9}";
        KafkaExporterLogEntry jsonRecord2 = mapper.readValue(logRecord2, KafkaExporterLogEntry.class);

        assertEquals(jsonRecord1.hashCode(), jsonRecord2.hashCode());
    }

}
