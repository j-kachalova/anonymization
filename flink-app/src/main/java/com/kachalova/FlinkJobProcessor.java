package com.kachalova;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kachalova.model.AnonymizationRuleDto;
import com.kachalova.model.JobResponseDto;
import com.kachalova.model.Person;
import com.kachalova.model.RuleSetResponseDto;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import org.apache.flink.api.common.serialization.SimpleStringSchema;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class FlinkJobProcessor {

    private static final String KAFKA_BROKER = "localhost:9092";

    public static void main(String[] args) throws Exception {

        // Создание окружения Flink
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Шаг 1: Получаем объект JobResponseDto из Kafka
        JobResponseDto jobResponse = getJobResponseFromKafka("job-requests");

        // Шаг 2: Настройка Kafka Source для входного топика
        Properties consumerProps = new Properties();
        consumerProps.setProperty("bootstrap.servers", KAFKA_BROKER);
        consumerProps.setProperty("group.id", "flink-group");

        // Создаем источник данных для чтения из Kafka
        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers(KAFKA_BROKER)
                .setTopics(jobResponse.getInputTopic())  // Входной топик
                .setGroupId("flink-group")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        // Чтение данных из Kafka
        DataStream<String> rawDataStream = env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "Kafka Source");

        // Шаг 3: Применение правил обезличивания
        DataStream<String> processedDataStream = rawDataStream.map(new MapFunction<String, String>() {
            @Override
            public String map(String value) throws Exception {
                // Преобразуем строку в объект (например, Person) если это JSON
                Person person = parseJsonToPerson(value);

                // Применяем правила из JobResponseDto
                for (AnonymizationRuleDto rule : jobResponse.getRuleSet().getRules()) {
                    switch (rule.getMethod()) {
                        case "MASK":
                            person = applyMasking(person, rule.getFieldName(), rule.getParameters());
                            break;
                        case "REMOVE":
                            person = applyRemoval(person, rule.getFieldName());
                            break;
                        // Можно добавить другие методы обезличивания
                        default:
                            throw new IllegalArgumentException("Неизвестный метод обезличивания: " + rule.getMethod());
                    }
                }

                // Преобразуем объект обратно в строку (например, JSON)
                return personToJson(person);
            }
        });

        // Шаг 4: Настройка Kafka Sink для выходного топика
        KafkaSink<String> kafkaSink = KafkaSink.<String>builder()
                .setBootstrapServers(KAFKA_BROKER)
                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                        .setTopic(jobResponse.getOutputTopic())  // Выходной топик
                        .setValueSerializationSchema(new SimpleStringSchema())
                        .build())
                .build();

        // Записываем обработанные данные в Kafka
        processedDataStream.sinkTo(kafkaSink);

        // Запуск задачи Flink
        env.execute("Job Processing with Anonymization");
    }

    // Получаем объект JobResponseDto из Kafka (например, из топика job-requests)
    private static JobResponseDto getJobResponseFromKafka(String topic) {
        // Логика для получения JobResponseDto из Kafka
        // Здесь будет код для получения сообщения из Kafka, десериализации в объект JobResponseDto
        // В реальной системе вы подключитесь к Kafka и получите задание из топика
        return JobResponseDto.builder()
                .id(UUID.randomUUID())
                .name("Обезличивание данных клиентов")
                .inputTopic("raw-pdn")
                .outputTopic("processed-pdn")
                .ruleSet(RuleSetResponseDto.builder()
                        .id(UUID.randomUUID())
                        .name("Obfuscation Rules")
                        .description("Правила обезличивания")
                        .rules(List.of(
                                AnonymizationRuleDto.builder().fieldName("phone").method("MASK").parameters("****").build(),
                                AnonymizationRuleDto.builder().fieldName("email").method("REMOVE").parameters("").build()
                        ))
                        .build())
                .schedule("0 0 * * *")
                .status("IN_PROGRESS")
                .build();
    }

    // Преобразование строки в объект (например, через Jackson)
    private static Person parseJsonToPerson(String value) throws JsonProcessingException {
        // Пример преобразования JSON строки в объект
        return new ObjectMapper().readValue(value, Person.class);
    }

    // Преобразование объекта обратно в JSON строку
    private static String personToJson(Person person) throws JsonProcessingException {
        // Пример преобразования объекта обратно в JSON
        return new ObjectMapper().writeValueAsString(person);
    }

    // Применение маски к полям объекта Person
    public static Person applyMasking(Person person, String fieldName, String mask) {
        switch (fieldName) {
            case "phone":
                person.setPhone(person.getPhone().replaceAll("(?<=\\d{3})\\d{4}", mask));
                break;
            case "email":
                person.setEmail(person.getEmail().replaceAll("(?<=.{3}).{4}", mask));
                break;
        }
        return person;
    }

    // Удаление поля из объекта Person
    public static Person applyRemoval(Person person, String fieldName) {
        switch (fieldName) {
            case "email":
                person.setEmail(null);
                break;
            case "phone":
                person.setPhone(null);
                break;
        }
        return person;
    }
}
