package jp.gr.java_conf.ogibayashi.prometheus;

import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.Consumer;
import java.util.Collection;

public class KteConsumerRebalanceListener implements ConsumerRebalanceListener {
    private Consumer<String,String> consumer;

    public KteConsumerRebalanceListener(Consumer<String,String> consumer) {
        this.consumer = consumer;
    }

    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
    }

    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
    }
}
 
 
