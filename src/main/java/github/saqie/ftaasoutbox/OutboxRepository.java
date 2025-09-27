package github.saqie.ftaasoutbox;

import java.util.List;

interface OutboxRepository {

    void save(Outbox outbox);

    List<Outbox> findUnprocessedOutboxes(int limit);

    void markAsPublished(List<Outbox> outboxes);

}
