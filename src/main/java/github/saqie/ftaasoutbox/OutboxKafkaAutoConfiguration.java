package github.saqie.ftaasoutbox;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;

@Configuration
@EnableConfigurationProperties(OutboxProperties.class)
@ConditionalOnClass(org.springframework.kafka.core.KafkaTemplate.class)
@ConditionalOnProperty(prefix = "outbox.kafka", name = "enabled", havingValue = "true", matchIfMissing = true)
class OutboxKafkaAutoConfiguration {

    @Bean
    KafkaTemplate<String, Outbox> outboxKafkaTemplate(ProducerFactory<String, Outbox> pf) {
        return new KafkaTemplate<>(pf);
    }

    @Bean
    ProducerFactory<String, Outbox> outboxProducerFactory(OutboxProperties props) {
        var cfg = new HashMap<String, Object>();
        cfg.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, props.getKafka().getBootstrapServers());
        cfg.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        cfg.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        cfg.put(org.springframework.kafka.support.serializer.JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        cfg.put(ProducerConfig.CLIENT_ID_CONFIG, props.getKafka().getClientId());
        cfg.putAll(props.getKafka().getProps());

        return new DefaultKafkaProducerFactory<>(cfg);
    }

    @Bean
    OutboxPublisher kafkaOutboxPublisher(KafkaTemplate<String, Outbox> template, OutboxProperties props) {
        return new OutboxKafkaPublisher(template, props.getKafka().getTopic());
    }

}
