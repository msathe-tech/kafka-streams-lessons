#!/usr/bin/env bash

export PATH=$PATH:/Users/msathe/workspace/confluent/confluent-5.3.1/bin

kafka-topics --delete --topic word-count-input --zookeeper localhost:2181
kafka-topics --delete --topic word-count-output --zookeeper localhost:2181

kafka-topics --list --zookeeper localhost:2181