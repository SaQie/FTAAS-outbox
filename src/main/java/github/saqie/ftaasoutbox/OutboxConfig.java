package github.saqie.ftaasoutbox;

import org.jooq.DSLContext;
import org.springframework.context.annotation.Configuration;

@Configuration
class OutboxConfig {

    OutboxRepository outboxWriter(DSLContext dslContext) {
        return new OutboxJooqRepository(dslContext);
    }

}
