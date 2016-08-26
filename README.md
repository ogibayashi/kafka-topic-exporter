# Kafka Topic Exporter

Consume Kafka topics and export to Prometheus

### Start process

```
java -jar kafka-topic-exporter-0.0.1-jar-with-dependencies.jar config/kafka-topic-exporter.properties
```

### Configuration

```
exporter.port=10040
bootstrap.servers=localhost:6667
group.id=test
# Java regex
kafka.consumer.topics=export\..*
kafka.consumer.remove.prefix=export\.aggregated_
```

### Record format

Each record in the topics should be the following format. `labels` are optional.

```
{"name": <metric name>,
 "value": <metric value>,
 "labels: {
    "foolabel": "foolabelvalue",
    "barlabel": "barlabelvalue"
    }
}
```

Then the following item will be exported.

```
<kafka topic name>_<metric_name>{foolabel="foolabelvalue", barlabel="barlabelvalue"} <metric value>
```
