package github.saqie.ftaasoutbox;

import github.saqie.ftaasoutbox.api.Event;
import github.saqie.ftaasoutbox.api.OutboxWriter;
import github.saqie.ftaasoutbox.api.Type;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
class OutboxWriterService implements OutboxWriter {

    private final JsonSerializer jsonSerializer;
    private final OutboxRepository outboxRepository;


    @Override
    public void write(Event event) {
        Type type = event.type();
        Payload payload = jsonSerializer.serialize(event);
        Outbox outbox = Outbox.from(type, payload);
        outboxRepository.save(outbox);
    }
}
