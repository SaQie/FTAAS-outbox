package github.saqie.ftaasoutbox;

import org.jooq.DSLContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static github.saqie.ftaas.outbox.jooq.tables.OutboxEvents.OUTBOX_EVENTS;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public abstract class PostgresContainer {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> PG = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private DSLContext dslContext;

    @Autowired
    protected OutboxRepository repository;

    @Autowired
    protected JsonSerializer jsonSerializer;

    @AfterEach
    void tearDown() {
        clearOutboxTable();
    }

    private void clearOutboxTable(){
        dslContext.deleteFrom(OUTBOX_EVENTS).execute();
    }

}
