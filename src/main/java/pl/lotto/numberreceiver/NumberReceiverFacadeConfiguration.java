package pl.lotto.numberreceiver;

import org.springframework.context.annotation.Configuration;
import java.time.Clock;

@Configuration
class NumberReceiverFacadeConfiguration {
    NumberReceiverFacade createModuleForTests(Clock clock, NumberReceiverRepository numberReceiverRepository) {
        NumbersReceiverValidator numbersReceiverValidator = new NumbersReceiverValidator();
        DateTimeDrawGenerator dateTimeDrawGenerator = new DateTimeDrawGenerator(clock);
        return new NumberReceiverFacade(numbersReceiverValidator, numberReceiverRepository, dateTimeDrawGenerator);
    }
}
