package com.example;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.ForeachAction;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// kafka-topics --create --topic word-count-output --zookeeper localhost:2181 --partitions 1 --replication-factor 1
// kafka-topics --create --topic word-count-input --zookeeper localhost:2181 --partitions 1 --replication-factor 1

// kafka-console-producer --broker-list localhost:9092 --topic word-count-input
// kafka-console-consumer --topic word-count-output --from-beginning --bootstrap-server localhost:9092 --property print.key=true --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer


// Delete
// kafka-topics --delete --topic word-count-input --zookeeper localhost:2181
// kafka-topics --delete --topic word-count-output --zookeeper localhost:2181
public class WordCount {
	static final Logger log = LoggerFactory.getLogger(WordCount.class.getName());
	public static void main(String[] args) {

		final String APP_NAME = "word-count";
		// Better to APP_VERSION if you change the code, the purpose is to change APPLICATION_ID_CONFIG
		final String APP_VERSION = "2.6";

		Properties props = new Properties();
		props.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, APP_NAME.concat(APP_VERSION));
		props.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
		props.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());

		final String INPUT_TOPIC = "word-count-input";
		final String OUTPUT_TOPIC = "word-count-output";
		StreamsBuilder builder = new StreamsBuilder();
		KStream<String, String> inputStream = builder.stream(INPUT_TOPIC);

		KTable<String, Long> wordCountTable = inputStream
				.mapValues(text -> text.toLowerCase())
				.flatMapValues(lowerCaseText -> Arrays.asList(lowerCaseText.split("\\W+")))
				.groupBy((key, word) -> word)
				.count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store"));

//		wordCountTable.toStream().foreach(new ForeachAction<String, Long>() {
//			@Override
//			public void apply(String s, Long aLong) {
//				System.out.println("word = " + s + ", value = " + aLong);
//			}
//		});

		wordCountTable.toStream().foreach((s, aLong) -> log.info("word = {}, value = {}", s, aLong));

		wordCountTable.toStream().to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.Long()));

		Topology topology = builder.build();

		KafkaStreams streams = new KafkaStreams(topology, props);
		streams.start();
		Runtime.getRuntime().addShutdownHook(new Thread(streams::close));



	}
}
