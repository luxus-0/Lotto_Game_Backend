package pl.lotto.numberreceiver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class NumberReceiverFacadeConfiguration {
    NumberReceiverFacade createModuleForTests(Clock clock) {
        NumbersReceiverValidator numbersReceiverValidator = new NumbersReceiverValidator();
        DateTimeDrawGenerator dateTimeDrawGenerator = new DateTimeDrawGenerator(clock);
        UUIDGenerator uuidGenerator = new UUIDGenerator();
        return new NumberReceiverFacade(numbersReceiverValidator, dateTimeDrawGenerator, uuidGenerator);
    }

    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }
}
