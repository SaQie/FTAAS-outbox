package github.saqie.ftaasoutbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
class JacksonJsonSerializer implements JsonSerializer {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Payload serialize(Object obj) {
        try {
            return Payload.create(mapper.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not serialize object", e);
        }
    }
}
