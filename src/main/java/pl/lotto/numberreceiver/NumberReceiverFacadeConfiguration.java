package pl.lotto.numberreceiver;

import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.LocalDateTime;

@Configuration
public class NumberReceiverFacadeConfiguration {

    NumberReceiverFacade createModuleForTests(Clock clock, LocalDateTime drawDate){
        DateTimeReceiver dateTimeReceiver = new DateTimeReceiver(clock);
        LocalDateTime dateTime = dateTimeReceiver.generateDrawDate(drawDate);
        NumbersReceiverValidator numbersValidator = new NumbersReceiverValidator();
        NumberReceiverRepository numberReceiverRepository = new InMemoryNumberReceiverRepository();
        return new NumberReceiverFacade(numbersValidator, numberReceiverRepository, dateTime);
    }
}
