package pl.lotto.numberreceiver;

import pl.lotto.date_time_generator.DateTimeGenerator;

public class NumberReceiverFacadeConfiguration {
    NumberReceiverFacade createModuleForTests(TicketRepository ticketRepository){
        NumbersValidator numbersValidator = new NumbersValidator();
        UuidGenerator uuidGenerator = new UuidGenerator();
        DateTimeGenerator dateTimeGenerator = new DateTimeGenerator();
        TicketGenerator ticketGenerator = new TicketGenerator(uuidGenerator, dateTimeGenerator);
        return new NumberReceiverFacade(numbersValidator, ticketRepository, ticketGenerator);
    }
}
