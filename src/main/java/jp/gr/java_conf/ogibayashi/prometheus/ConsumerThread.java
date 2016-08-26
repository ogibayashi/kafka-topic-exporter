package jp.gr.java_conf.ogibayashi.prometheus;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

class ConsumerThread implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(ConsumerThread.class);

    private PropertyConfig props;
    private KafkaCollector collector;
    
    public ConsumerThread(KafkaCollector collector, PropertyConfig props) {
        this.collector = collector;
        this.props = props;
    }
    
    @Override
    public void run() {

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props.getProperties());
        consumer.subscribe(props.getTopicsPattern(), new KteConsumerRebalanceListener(consumer));
        try {
            while (!Thread.interrupted()) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    String topic = record.topic();
                    if(props.get(PropertyConfig.Constants.KAKFA_CONSUER_REMOVEPREFIX.key,null) != null) {
                        topic = topic.replaceFirst("^" + props.get(PropertyConfig.Constants.KAKFA_CONSUER_REMOVEPREFIX.key), "");
                    }
                    collector.add(topic, record.value());
                }
            }
        }
        finally {
            LOG.info("Shutting down");
            consumer.close();
        }
    }
}
