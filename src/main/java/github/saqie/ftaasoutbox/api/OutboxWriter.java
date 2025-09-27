package github.saqie.ftaasoutbox.api;

public interface OutboxWriter {

    void write(Event event);

}
