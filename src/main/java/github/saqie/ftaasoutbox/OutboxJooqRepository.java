package github.saqie.ftaasoutbox;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

import static github.saqie.ftaas.jooq.tables.OutboxEvents.OUTBOX_EVENTS;

@AllArgsConstructor
@Repository
class OutboxJooqRepository implements OutboxRepository {

    private final DSLContext dslContext;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void save(Outbox outbox) {
        dslContext.insertInto(OUTBOX_EVENTS)
                .set(OUTBOX_EVENTS.EVENT_ID, outbox.eventId().id())
                .set(OUTBOX_EVENTS.ATTEMPTS, outbox.attempts().attempt())
                .set(OUTBOX_EVENTS.TYPE, outbox.type().value())
                .set(OUTBOX_EVENTS.CREATED_AT, OffsetDateTime.now())
                .set(OUTBOX_EVENTS.PAYLOAD_JSON, outbox.payload().json())
                .set(OUTBOX_EVENTS.STATUS, outbox.status().rawStatus())
                .execute();
    }


    @Override
    public List<Outbox> findUnprocessedOutboxes(final int limit) {
        return dslContext.selectFrom(OUTBOX_EVENTS)
                .where(OUTBOX_EVENTS.TYPE.eq(Status.Outbox.NEW.name()))
                .orderBy(OUTBOX_EVENTS.CREATED_AT.asc())
                .limit(limit)
                .forUpdate()
                .skipLocked()
                .fetch(OutboxMapper::toDomain);
    }

    @Override
    public void markAsPublished(final List<Outbox> outboxes) {
        final var ids = outboxes.stream()
                .map(outbox -> outbox.eventId().id())
                .toList();

        dslContext.update(OUTBOX_EVENTS)
                .set(OUTBOX_EVENTS.STATUS, Status.Outbox.PUBLISHED.name())
                .where(OUTBOX_EVENTS.EVENT_ID.in(ids))
                .execute();

    }
}
