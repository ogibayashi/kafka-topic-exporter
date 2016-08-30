package jp.gr.java_conf.ogibayashi.prometheus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.Consumer;
import java.util.Collection;

public class KteConsumerRebalanceListener implements ConsumerRebalanceListener {
    private static final Logger LOG = LoggerFactory.getLogger(KteConsumerRebalanceListener.class);
    private Consumer<String,String> consumer;

    public KteConsumerRebalanceListener(Consumer<String,String> consumer) {
        this.consumer = consumer;
    }

    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        for (TopicPartition t: partitions) {
            LOG.info("Revoked partition. topic: {}, partition: {}", t.topic(), t.partition());
        }
    }

    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        for (TopicPartition t: partitions) {
            LOG.info("Assigned partition. topic: {}, partition: {}", t.topic(), t.partition());
        }
    }
}
 
 
