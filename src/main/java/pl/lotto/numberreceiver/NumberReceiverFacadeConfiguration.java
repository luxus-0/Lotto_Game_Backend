package pl.lotto.numberreceiver;

import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class NumberReceiverFacadeConfiguration {
    NumberReceiverFacade createModuleForTests(Clock clock, NumberReceiverRepository numberReceiverRepository) {
        NumbersReceiverValidator numbersValidator = new NumbersReceiverValidator();
        DateReceiverValidator dateReceiverValidator = new DateReceiverValidator();
        return new NumberReceiverFacade(clock, numbersValidator, dateReceiverValidator, numberReceiverRepository);
    }
}
