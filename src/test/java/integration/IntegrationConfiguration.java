package integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import pl.lotto.domain.drawdate.AdjustableClock;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Configuration
@Profile("integration")
public class IntegrationConfiguration {

    @Bean
    @Primary
    AdjustableClock clock() {
        return new AdjustableClock(LocalDateTime.of(2022, 11, 16, 10, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
    }
}