package com.kachalova.source;

import com.kachalova.model.JobResponseDto;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;

import java.util.regex.Pattern;

public class KafkaSourceProvider {

    private static final String BOOTSTRAP_SERVERS = "kafka:9092";

    public static KafkaSource<JobResponseDto> createJobKafkaSource() {
        return KafkaSource.<JobResponseDto>builder()
                .setBootstrapServers(BOOTSTRAP_SERVERS)
                .setTopics("job-commands")
                .setGroupId("job-group")
                .setValueOnlyDeserializer(new com.kachalova.deserialization.JobResponseDtoDeserializer())
                .setStartingOffsets(OffsetsInitializer.earliest())
                .build();
    }

    public static KafkaSource<String> createDataKafkaSourceWildcard() {
        return KafkaSource.<String>builder()
                .setBootstrapServers(BOOTSTRAP_SERVERS)
                .setTopicPattern(Pattern.compile("raw-.*"))
                .setGroupId("data-group")
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .setStartingOffsets(OffsetsInitializer.earliest())
                .build();
    }

}
