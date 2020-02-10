FROM openjdk:8-jre-alpine
COPY target/kafka-topic-exporter-0.0.6-jar-with-dependencies.jar app.jar
COPY config/kafka-topic-exporter.sample.properties exporter.properties
CMD ["/usr/bin/java", "-jar", "app.jar", "exporter.properties"]
