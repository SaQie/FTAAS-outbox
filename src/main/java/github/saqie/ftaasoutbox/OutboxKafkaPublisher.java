package github.saqie.ftaasoutbox;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
record OutboxKafkaPublisher(
        KafkaTemplate<String, Outbox> kafkaTemplate,
        String topic
) implements OutboxPublisher {

    @Override
    public void publish(final Outbox outbox) {
        final var id = outbox.eventId().id().toString();
        kafkaTemplate.send(topic, id, outbox);
    }
}
