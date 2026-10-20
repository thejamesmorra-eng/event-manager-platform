package dev.sorokin.eventmanager.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${kafka.topic.event-changes}")
    private String eventChangesTopic;

    @Bean
    public NewTopic eventChangesTopic() {
        return TopicBuilder.name(eventChangesTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
