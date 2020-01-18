#!/usr/bin/env bash
export PATH=$PATH:/Users/msathe/workspace/confluent/confluent-5.3.1/bin

kafka-topics --create --topic word-count-output --zookeeper localhost:2181 --partitions 1 --replication-factor 1
kafka-topics --create --topic word-count-input --zookeeper localhost:2181 --partitions 1 --replication-factor 1

kafka-topics --list --zookeeper localhost:2181