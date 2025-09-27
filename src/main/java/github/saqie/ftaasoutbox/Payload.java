package github.saqie.ftaasoutbox;

import org.jooq.JSON;

record Payload(
        String value
) {

    static Payload create(String payload) {
        return new Payload(payload);
    }

    JSON json() {
        return JSON.json(value);
    }
}
