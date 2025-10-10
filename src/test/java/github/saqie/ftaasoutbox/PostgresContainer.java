package github.saqie.ftaasoutbox;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class PostgresContainer {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> PG = new PostgreSQLContainer<>("postgres:16");

}
