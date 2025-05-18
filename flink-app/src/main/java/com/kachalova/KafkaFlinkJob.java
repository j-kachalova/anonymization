package com.kachalova;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kachalova.model.JobResponseDto;
import com.kachalova.model.RuleSetResponseDto;
import com.kachalova.model.AnonymizationRuleDto;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import org.apache.flink.streaming.api.datastream.DataStream;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.functions.sink.legacy.PrintSinkFunction;
import org.apache.flink.util.Collector;

import java.io.IOException;

public class KafkaFlinkJob {

    public static void main(String[] args) throws Exception {

        // Создаем среду выполнения
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Настройка источника данных из Kafka для JobResponseDto
        KafkaSource<JobResponseDto> jobSource = KafkaSource.<JobResponseDto>builder()
                .setBootstrapServers("localhost:9092") // Адрес Kafka
                .setTopics("your-job-topic") // Название топика для JobResponseDto
                .setGroupId("flink-consumer-group") // ID группы
                .setStartingOffsets(org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer.earliest()) // Начало чтения с самого первого сообщения
                .setValueOnlyDeserializer(new JobResponseDtoDeserializer()) // Десериализатор для JobResponseDto
                .build();

        // Чтение данных JobResponseDto из Kafka
        DataStream<JobResponseDto> jobStream = env.fromSource(jobSource, WatermarkStrategy.noWatermarks(), "Job Source");

        // Для каждого JobResponseDto, извлекаем inputTopic и читаем из этого топика
        jobStream.flatMap((JobResponseDto job, Collector<String> out) -> {
            String inputTopic = job.getInputTopic();

            // Создаем KafkaSource для inputTopic
            KafkaSource<String> inputTopicSource = KafkaSource.<String>builder()
                    .setBootstrapServers("localhost:9092")
                    .setTopics(inputTopic) // Используем inputTopic из JobResponseDto
                    .setGroupId("flink-consumer-group-input") // Группа для inputTopic
                    .setStartingOffsets(org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer.earliest())
                    .setValueOnlyDeserializer(new SimpleStringDeserializer()) // Простой десериализатор строк
                    .build();

            // Чтение данных из inputTopic
            DataStream<String> inputTopicStream = env.fromSource(inputTopicSource, WatermarkStrategy.noWatermarks(), "Input Topic Source");

            // Обработка данных из inputTopic
            inputTopicStream.map(message -> "Processed from inputTopic: " + message)
                    .addSink(new PrintSinkFunction<>());
        }).setParallelism(1);

        // Запуск джобы
        env.execute("Flink Job Reading from Dynamic Kafka Topics");
    }

    // Десериализатор для JobResponseDto
    public static class JobResponseDtoDeserializer implements DeserializationSchema<JobResponseDto> {

        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public JobResponseDto deserialize(byte[] message) throws IOException {
            return objectMapper.readValue(message, JobResponseDto.class);
        }

        @Override
        public boolean isEndOfStream(JobResponseDto nextElement) {
            return false;
        }

        @Override
        public TypeInformation<JobResponseDto> getProducedType() {
            return TypeInformation.of(JobResponseDto.class);
        }
    }

    // Простой десериализатор для строк
    public static class SimpleStringDeserializer implements DeserializationSchema<String> {

        @Override
        public String deserialize(byte[] message) throws IOException {
            return new String(message);
        }

        @Override
        public boolean isEndOfStream(String nextElement) {
            return false;
        }

        @Override
        public TypeInformation<String> getProducedType() {
            return TypeInformation.of(String.class);
        }
    }
}
