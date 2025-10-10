package github.saqie.ftaasoutbox;

import github.saqie.ftaasoutbox.api.Type;

import java.time.LocalDateTime;
import java.util.UUID;

public class FakeOutboxBuilder {

    private EventId eventId;
    private Status status;
    private Attempts attempts;
    private Payload payload;
    private Type type;
    private LocalDateTime occurredAt;

    public static FakeOutboxBuilder builder() {
        return new FakeOutboxBuilder();
    }

    private FakeOutboxBuilder() {
        this.eventId = EventId.create();
        this.status = Status.createNew();
        this.attempts = Attempts.zero();
        this.payload = Payload.create(UUID.randomUUID().toString());
        this.occurredAt = LocalDateTime.now();
    }


    public FakeOutboxBuilder withEventId(EventId eventId) {
        this.eventId = eventId;
        return this;
    }


    public FakeOutboxBuilder withStatus(Status status) {
        this.status = status;
        return this;
    }

    public FakeOutboxBuilder withAttempts(Attempts attempts) {
        this.attempts = attempts;
        return this;
    }

    public FakeOutboxBuilder withPayload(Payload payload) {
        this.payload = payload;
        return this;
    }

    public FakeOutboxBuilder withType(Type type) {
        this.type = type;
        return this;
    }

    public FakeOutboxBuilder withOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
        return this;
    }

    public Outbox build() {
        return new Outbox(
                eventId,
                status,
                attempts,
                payload,
                type,
                occurredAt
        );
    }

}
