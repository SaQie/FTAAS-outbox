package github.saqie.ftaasoutbox;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
class OutboxProperties {

    private int batchSize = 100;
    private Duration poolIntervalMs = Duration.ofSeconds(10);
    private Duration initialDelayMs = Duration.ZERO;
    private int attemptLimit = 3;
    private Kafka kafka = new Kafka();

    @Getter
    @Setter
    static class Kafka {
        private boolean enabled = true;
        private String bootstrapServers;
        private String topic;
        private String clientId = "ftaas-outbox";
        private Map<String, String> props = new HashMap<>();
    }

}
