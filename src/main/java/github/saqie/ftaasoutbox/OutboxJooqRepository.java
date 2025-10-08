package github.saqie.ftaasoutbox;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static github.saqie.ftaas.outbox.jooq.tables.OutboxEvents.OUTBOX_EVENTS;

@AllArgsConstructor
@Repository
class OutboxJooqRepository implements OutboxRepository {

    private final DSLContext dslContext;

    @Override
    public void save(Outbox outbox) {
        dslContext.insertInto(OUTBOX_EVENTS)
                .set(OutboxMapper.fromDomain(outbox))
                .execute();
    }


    @Override
    public List<Outbox> findUnprocessedOutboxes(final int limit) {
        return dslContext.selectFrom(OUTBOX_EVENTS)
                .where(OUTBOX_EVENTS.STATUS.eq(Status.Outbox.NEW.name()))
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
