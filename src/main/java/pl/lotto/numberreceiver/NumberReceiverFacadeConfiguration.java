package pl.lotto.numberreceiver;

import pl.lotto.date_time_generator.DateTimeGenerator;

import java.time.Clock;

public class NumberReceiverFacadeConfiguration {
    NumberReceiverFacade createModuleForTests(TicketRepository ticketRepository, Clock clock){
        NumbersValidator numbersValidator = new NumbersValidator();
        UuidGenerator uuidGenerator = new UuidGenerator();
        DateTimeGenerator dateTimeGenerator = new DateTimeGenerator(clock);
        TicketGenerator ticketGenerator = new TicketGenerator(uuidGenerator, dateTimeGenerator);
        return new NumberReceiverFacade(numbersValidator, ticketRepository, ticketGenerator);
    }
}
