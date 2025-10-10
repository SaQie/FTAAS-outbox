package github.saqie.ftaasoutbox;

import github.saqie.ftaasoutbox.api.Event;
import github.saqie.ftaasoutbox.api.Type;

public class FakeEventBuilder {

    public static Event build(Type type) {
        return new FakeEvent(type);
    }

    public static Event build(String type) {
        return new FakeEvent(Type.create(type));
    }


    public record FakeEvent(
            Type type
    ) implements Event {
        @Override
        public Type type() {
            return type;
        }
    }
}
