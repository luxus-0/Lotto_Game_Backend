package pl.lotto.numberreceiver;

import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class NumberReceiverFacadeConfiguration {
    NumberReceiverFacade createModuleForTests(Clock clock, NumberReceiverRepository numberReceiverRepository) {
        NumbersReceiverValidator numbersValidator = new NumbersReceiverValidator();
        DateTimeDrawGenerator dateTimeDrawGenerator = new DateTimeDrawGenerator(clock);
        UUIDGenerator uuidGenerator = new UUIDGenerator();
        return new NumberReceiverFacade(clock, numbersValidator, numberReceiverRepository, dateTimeDrawGenerator, uuidGenerator);
    }
}
