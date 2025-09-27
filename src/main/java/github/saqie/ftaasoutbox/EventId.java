package github.saqie.ftaasoutbox;

import java.util.UUID;

record EventId(
        UUID id
) {
    static EventId create() {
        return new EventId(UUID.randomUUID());
    }
}
