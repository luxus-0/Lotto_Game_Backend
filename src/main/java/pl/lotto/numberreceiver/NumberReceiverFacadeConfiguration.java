package pl.lotto.numberreceiver;

import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.LocalDateTime;

@Configuration
public class NumberReceiverFacadeConfiguration {

    NumberReceiverFacade createModuleForTests(NumberReceiverGenerator numberReceiverGenerator, Clock clock) {
        DateTimeReceiverValidator dateTimeReceiver = new DateTimeReceiverValidator(clock);
        NumbersReceiverValidator numbersValidator = new NumbersReceiverValidator();
        NumberReceiverRepository numberReceiverRepository = new InMemoryNumberReceiverRepository();
        return new NumberReceiverFacade(numbersValidator, dateTimeReceiver, numberReceiverRepository, numberReceiverGenerator);
    }
}
