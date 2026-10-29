package dev.sorokin.eventnotificator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sorokin.eventcommon.kafka.dto.EventChangeKafkaMessageDto;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;
import java.util.UUID;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    public ConsumerFactory<UUID, EventChangeKafkaMessageDto> consumerFactory(
            KafkaProperties kafkaProperties,
            ObjectMapper objectMapper
    ) {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();

        JsonDeserializer<EventChangeKafkaMessageDto> valueDeserializer =
                new JsonDeserializer<>(EventChangeKafkaMessageDto.class, objectMapper);

        DefaultKafkaConsumerFactory<UUID, EventChangeKafkaMessageDto> factory =
                new DefaultKafkaConsumerFactory<>(props);
        factory.setValueDeserializer(valueDeserializer);

        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<UUID, EventChangeKafkaMessageDto> kafkaListenerContainerFactory(
            ConsumerFactory<UUID, EventChangeKafkaMessageDto> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<UUID, EventChangeKafkaMessageDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        return factory;
    }
}
