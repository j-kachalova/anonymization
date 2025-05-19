package com.kachalova.fileprocessing.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class TopicService {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public void createTopic(String topicName, int partitions, short replicationFactor) {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        try (AdminClient adminClient = AdminClient.create(props)) {
            NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);
            adminClient.createTopics(Collections.singleton(newTopic)).all().get();
        } catch (Exception e) {
            if (e.getCause() instanceof org.apache.kafka.common.errors.TopicExistsException) {
                // Игнорируем, если топик уже есть
            } else {
                throw new RuntimeException("Не удалось создать топик: " + topicName, e);
            }
        }
    }
}
