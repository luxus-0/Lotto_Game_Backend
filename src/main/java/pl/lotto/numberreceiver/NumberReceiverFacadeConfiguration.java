package pl.lotto.numberreceiver;

import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class NumberReceiverFacadeConfiguration {
    NumberReceiverFacade createModuleForTests(Clock clock) {
        NumbersReceiverValidator numbersReceiverValidator = new NumbersReceiverValidator();
        DateTimeDrawGenerator dateTimeDrawGenerator = new DateTimeDrawGenerator(clock);
        NumberReceiverRepository numberReceiverRepository = new InMemoryNumberReceiverRepository(dateTimeDrawGenerator);
        return new NumberReceiverFacade(numbersReceiverValidator, numberReceiverRepository, dateTimeDrawGenerator);
    }
}
