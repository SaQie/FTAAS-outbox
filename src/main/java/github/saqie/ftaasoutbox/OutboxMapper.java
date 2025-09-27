package github.saqie.ftaasoutbox;

import github.saqie.ftaas.jooq.tables.records.OutboxEventsRecord;
import github.saqie.ftaas.projectenv.outbox.api.Type;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class OutboxMapper {

    static Outbox toDomain(OutboxEventsRecord record) {
        return Outbox.from(Type.create(record.getType()), Payload.create(record.getPayloadJson().data()));
    }

    static OutboxEventsRecord fromDomain(Outbox outbox) {
        final var record = new OutboxEventsRecord();
        record.setAttempts(outbox.attempts().attempt());
        record.setCreatedAt(OffsetDateTime.now());
        record.setType(outbox.type().value());
        record.setEventId(outbox.eventId().id());
        record.setPayloadJson(outbox.payload().json());
        record.setStatus(outbox.status().rawStatus());
        return record;
    }
}
