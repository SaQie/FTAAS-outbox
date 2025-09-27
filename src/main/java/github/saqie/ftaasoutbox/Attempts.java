package github.saqie.ftaasoutbox;

record Attempts(
        Integer attempt
) {
    static Attempts zero() {
        return new Attempts(0);
    }
}
