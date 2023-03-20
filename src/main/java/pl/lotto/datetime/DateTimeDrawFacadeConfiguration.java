package pl.lotto.datetime;

import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class DateTimeDrawFacadeConfiguration {
    DateTimeDrawFacade createModuleForTests(Clock clock) {
        return new DateTimeDrawFacade(clock);
    }
}
