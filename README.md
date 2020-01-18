# Kafka Streams Lessons
**Prerequisites** 
* Make sure you have Kafka setup running on local machine
* Make sure you have broker accessible on localhost:9092
* Make sure you have schema registry accessible on http://localhost:8081

## Word Count
[Module Folder](https://github.com/msathe-tech/kafka-streams-lessons/tree/master/word-count)

[Source code](https://github.com/msathe-tech/kafka-streams-lessons/blob/master/word-count/src/main/java/com/example/WordCount.java)

**IMP**
* Add Confluent bin directory to PATH
* Edit [setup-word-count-topics.sh](https://github.com/msathe-tech/kafka-streams-lessons/blob/master/word-count/setup-word-count-topics.sh) 
to change the PATH of Confluent bin directory. 
* Edit [cleanup-word-count-topics.sh](https://github.com/msathe-tech/kafka-streams-lessons/blob/master/word-count/cleanup-word-count-topics.sh) 
to change the PATH of Confluent bin directory. 

*How to run*
* `cd word-count`
* `./setup-word-count-topics.sh`
* From one shell - `kafka-console-producer --broker-list localhost:9092 --topic word-count-input`
* From another shell - `kafka-console-consumer --topic word-count-output --from-beginning --bootstrap-server localhost:9092 --property print.key=true --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer`
* Run the WordCount.main()

