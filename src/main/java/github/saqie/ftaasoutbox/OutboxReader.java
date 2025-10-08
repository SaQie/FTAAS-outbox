package github.saqie.ftaasoutbox;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
class OutboxReader {

    private final OutboxRepository outboxRepository;
    private final OutboxProperties outboxProperties;
    private final OutboxPublisher outboxPublisher;


    @Scheduled(
            fixedDelayString = "${outbox.pool-interval-ms:10000}",
            initialDelayString = "${outbox.initial-delay-ms:0}"
    )
    public void process() {
        log.info("Start reading outbox");
        log.info("Batch size: {}", outboxProperties.getBatchSize());
        final var unprocessedOutboxes = outboxRepository.findUnprocessedOutboxes(outboxProperties.getBatchSize());
        log.info("Unprocessed outboxes: {}", unprocessedOutboxes);
        unprocessedOutboxes.forEach(outbox -> {
            log.info("Publishing event {}", outbox.eventId());
            outboxPublisher.publish(outbox);
        });
        outboxRepository.markAsPublished(unprocessedOutboxes);
    }

}
