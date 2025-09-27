package github.saqie.ftaasoutbox;


import github.saqie.ftaasoutbox.api.Type;

import java.time.LocalDateTime;

record Outbox(
        EventId eventId,
        Status status,
        Attempts attempts,
        Payload payload,
        Type type,
        LocalDateTime occurredAt
) {

    static Outbox from(Type type, Payload payload) {
        return new Outbox(
                EventId.create(),
                Status.createNew(),
                Attempts.zero(),
                payload,
                type,
                LocalDateTime.now());
    }

    @Override
    public String toString() {
        return "Outbox: [" + eventId.id() + "] Attempt: [" + attempts.attempt() + "] Type: [" + type.value() + "] Payload: [" + payload.value() + "] Status: [" + status.rawStatus() + "] Occurred at: [" + occurredAt + "]";
    }
}
