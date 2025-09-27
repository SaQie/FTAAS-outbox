package github.saqie.ftaasoutbox;

record Status(
        Outbox status
) {

    static Status createNew() {
        return new Status(Outbox.NEW);
    }

    static Status createPublished() {
        return new Status(Outbox.PUBLISHED);
    }

    static Status createFailed() {
        return new Status(Outbox.FAILED);
    }

    static Status createOk() {
        return new Status(Outbox.OK);
    }

    String rawStatus() {
        return status.name();
    }

    enum Outbox {
        NEW,
        PUBLISHED,
        FAILED,
        OK
    }

}
