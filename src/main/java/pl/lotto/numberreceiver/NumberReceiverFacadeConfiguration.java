package pl.lotto.numberreceiver;

import org.springframework.context.annotation.Configuration;
import pl.lotto.datetimegenerator.DateTimeDrawFacade;

import java.time.Clock;

@Configuration
class NumberReceiverFacadeConfiguration {
    NumberReceiverFacade createModuleForTests(Clock clock) {
        NumbersReceiverValidator numbersReceiverValidator = new NumbersReceiverValidator();
        DateTimeDrawFacade dateTimeDrawFacade = new DateTimeDrawFacade(clock);
        TicketRepository ticketRepository = new InMemoryTicketRepository();
        HashGenerator hashGenerator = new HashGenerator();
        return new NumberReceiverFacade(numbersReceiverValidator, dateTimeDrawFacade, ticketRepository, hashGenerator);
    }
}
