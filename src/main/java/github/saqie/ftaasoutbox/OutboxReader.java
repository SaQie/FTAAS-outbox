package github.saqie.ftaasoutbox;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class OutboxReader {

    private final OutboxRepository outboxRepository;
    private final OutboxProperties outboxProperties;
    private final OutboxPublisher outboxPublisher;

    OutboxReader(final OutboxRepository outboxRepository, final OutboxProperties outboxProperties, final OutboxPublisher outboxPublisher) {
        this.outboxRepository = outboxRepository;
        this.outboxProperties = outboxProperties;
        this.outboxPublisher = outboxPublisher;
    }

    @Scheduled(
            fixedDelayString = "#{@outboxProperties.poolIntervalMs.toMillis()}",
            initialDelayString = "#{@outboxProperties.initialDelayMs.toMillis()}"
    )
    public void process() {
        final var unprocessedOutboxes = outboxRepository.findUnprocessedOutboxes(outboxProperties.getBatchSize());
        unprocessedOutboxes.forEach(outbox -> {
            log.info("Publishing event {}", outbox.eventId());
            outboxPublisher.publish(outbox);
        });
        outboxRepository.markAsPublished(unprocessedOutboxes);
    }

}
