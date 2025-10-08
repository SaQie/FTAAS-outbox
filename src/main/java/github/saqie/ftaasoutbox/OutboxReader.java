package github.saqie.ftaasoutbox;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
class OutboxReader {

    private final OutboxRepository outboxRepository;
    private final OutboxProperties outboxProperties;
    private final OutboxPublisher outboxPublisher;


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
