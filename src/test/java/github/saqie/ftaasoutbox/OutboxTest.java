package github.saqie.ftaasoutbox;

import github.saqie.ftaasoutbox.api.OutboxWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OutboxTest extends PostgresContainer {


    private OutboxWriter outboxWriter;

    @BeforeEach
    void setUp() {
        this.outboxWriter = new OutboxWriterService(jsonSerializer, repository);
    }

    @Test
    @DisplayName("Should successfully save outbox")
    void test01(){
        // given
        var event = FakeEventBuilder.build("Test");

        // when
        outboxWriter.write(event);

        // then
        var unprocessedOutboxes = repository.findUnprocessedOutboxes(10);
        assertThat(unprocessedOutboxes).hasSize(1);
    }

}
