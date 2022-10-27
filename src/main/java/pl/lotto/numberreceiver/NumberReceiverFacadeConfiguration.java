package pl.lotto.numberreceiver;

import org.springframework.context.annotation.Configuration;
import pl.lotto.datetimegenerator.*;

@Configuration
public class NumberReceiverFacadeConfiguration {

    NumberReceiverFacade createModuleForTests(TicketRepository ticketRepository, DateTimeGenerator timeGenerator){
        NumbersValidator numbersValidator = new NumbersValidator();
        UuidGenerator uuidGenerator = new UuidGenerator();
        DateTimeActualGenerator dateTimeActualGenerator = new DateTimeActualGenerator(timeGenerator.getClock());
        DateTimeDrawGenerator dateTimeDrawGenerator = new DateTimeDrawGenerator(timeGenerator.getDrawHour(), timeGenerator.getDrawDayOfWeek());
        DateTimeExpiryGenerator dateTimeExpiryGenerator = new DateTimeExpiryGenerator(timeGenerator.getExpirationInDays());
        DateTimeGeneratorFacade dateTimeGeneratorFacade = new DateTimeGeneratorFacade(dateTimeActualGenerator, dateTimeDrawGenerator, dateTimeExpiryGenerator);
        TicketGenerator ticketCreated = new TicketGenerator(uuidGenerator, dateTimeGeneratorFacade);
        return new NumberReceiverFacade(numbersValidator, ticketRepository, ticketCreated);
    }
}
