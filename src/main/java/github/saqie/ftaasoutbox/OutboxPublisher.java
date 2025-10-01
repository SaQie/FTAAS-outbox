package github.saqie.ftaasoutbox;

interface OutboxPublisher {

    void publish(Outbox outbox);

}
