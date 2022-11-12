package pl.lotto.numberreceiver;

import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class NumberReceiverFacadeConfiguration {

    NumberReceiverFacade createModuleForTests(NumberReceiverGenerator numberReceiverGenerator, Clock clock) {
        DateTimeReceiver dateTimeReceiver = new DateTimeReceiver(clock);
        NumbersReceiverValidator numbersValidator = new NumbersReceiverValidator();
        NumberReceiverRepository numberReceiverRepository = new InMemoryNumberReceiverRepository();
        return new NumberReceiverFacade(numbersValidator, numberReceiverRepository, numberReceiverGenerator);
    }
}
