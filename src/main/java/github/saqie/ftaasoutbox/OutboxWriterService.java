package github.saqie.ftaasoutbox;

import github.saqie.ftaasoutbox.api.Event;
import github.saqie.ftaasoutbox.api.OutboxWriter;
import github.saqie.ftaasoutbox.api.Type;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
class OutboxWriterService implements OutboxWriter {

    private final JsonSerializer jsonSerializer;
    private final OutboxRepository outboxRepository;


    @Override
    public void write(Event event) {
        Type type = event.type();
        Payload payload = jsonSerializer.serialize(event);
        Outbox outbox = Outbox.from(type, payload);
        log.info("Writing outbox event[{}]: {}", outbox.eventId().id(), outbox);
        outboxRepository.save(outbox);
    }
}
