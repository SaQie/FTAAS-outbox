package github.saqie.ftaasoutbox;

import github.saqie.ftaasoutbox.api.OutboxWriter;
import liquibase.integration.spring.SpringLiquibase;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;


@AutoConfiguration(after = JooqAutoConfiguration.class)
public class OutboxAutoConfiguration {

    @Bean
    @ConditionalOnClass(SpringLiquibase.class)
    @ConditionalOnBean(DataSource.class)
    @ConditionalOnProperty(prefix = "outbox.liquibase", name = "enabled", havingValue = "true", matchIfMissing = true)
    SpringLiquibase outboxLiquibase(DataSource dataSource) {
        SpringLiquibase lb = new SpringLiquibase();
        lb.setDataSource(dataSource);
        lb.setChangeLog("classpath:db/changelog/db.changelog-master.xml");
        lb.setShouldRun(true);
        lb.setLiquibaseTablespace(null);
        return lb;
    }

    @Bean
    @ConditionalOnBean(JsonSerializer.class)
    @ConditionalOnMissingBean(OutboxWriter.class)
    OutboxWriter outboxWriter(final JsonSerializer jsonSerializer, final OutboxRepository outboxRepository) {
        return new OutboxWriterService(jsonSerializer, outboxRepository);
    }

    @Bean
    @ConditionalOnClass(DSLContext.class)
    @ConditionalOnBean(DSLContext.class)
    @ConditionalOnMissingBean(OutboxRepository.class)
    OutboxRepository outboxRepositoryJooq(DSLContext dsl) {
        return new OutboxJooqRepository(dsl);
    }
}
