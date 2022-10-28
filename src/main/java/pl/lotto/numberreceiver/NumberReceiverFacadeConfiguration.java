package pl.lotto.numberreceiver;

import org.springframework.context.annotation.Configuration;

@Configuration
public class NumberReceiverFacadeConfiguration {

    NumberReceiverFacade createModuleForTests(TicketRepository ticketRepository, TicketCurrentDateTime currentDateTime){
        NumbersValidator numbersValidator = new NumbersValidator();
        TicketRandomUUID uuid = new TicketRandomUUID();
        TicketDrawDate drawDate = new TicketDrawDate(currentDateTime);
        TicketGenerator ticketCreated = new TicketGenerator(uuid, drawDate);
        return new NumberReceiverFacade(numbersValidator, ticketRepository, ticketCreated);
    }
}
