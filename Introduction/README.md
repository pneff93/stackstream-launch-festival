# Introduction

We talk about the upcoming topics as well as set up Apache Kafka locally.

## Run

```shell
docker-compose up -d
```
Starts Kafka Broker, Zookeeper, and control center.
In the control center, we can create topics, produce and consume events.

```shell
docker-compose exec broker bash

kafka-console-producer --topic test-topic --bootstrap-server broker:9092 --property parse.key=true  --property key.separator=":"
kafka-console-consumer --bootstrap-server broker:9092 --topic test-topic
```

## Sources

* https://github.com/confluentinc/cp-all-in-one