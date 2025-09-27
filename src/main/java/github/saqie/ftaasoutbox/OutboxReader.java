package github.saqie.ftaasoutbox;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
class OutboxReader {

    private final OutboxRepository outboxRepository;
    private final int outboxBatchSize;

    OutboxReader(final OutboxRepository outboxRepository, @Value("${outbox.batch.size}") final int outboxBatchSize) {
        this.outboxRepository = outboxRepository;
        this.outboxBatchSize = outboxBatchSize;
    }

    @Scheduled(
            fixedDelayString = "${outbox.poll-interval-ms}",
            initialDelayString = "${outbox.initial-delay-ms}"
    )
    @Transactional
    void process() {
        final var unprocessedOutboxes = outboxRepository.findUnprocessedOutboxes(outboxBatchSize);
        unprocessedOutboxes.forEach(outbox -> {
            log.info("Processing outbox {}", outbox);
            // TODO -> kafka/rabbitMQ
        });
        outboxRepository.markAsPublished(unprocessedOutboxes);
    }

}
