package dev.sorokin.eventnotificator.kafka;

import dev.sorokin.eventcommon.kafka.EventChangeKafkaMessageDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EventKafkaListenerService {

    @KafkaListener(topics = "${kafka.topic.event-changes}")
    public void listenEventChange(ConsumerRecord<UUID, EventChangeKafkaMessageDto> record) {
    }
}
