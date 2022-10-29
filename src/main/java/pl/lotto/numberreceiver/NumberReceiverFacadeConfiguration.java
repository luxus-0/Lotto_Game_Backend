package pl.lotto.numberreceiver;

import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class NumberReceiverFacadeConfiguration {

    NumberReceiverFacade createModuleForTests(TicketRepository ticketRepository, TicketCurrentDateTime ticketCurrentDateTime, LocalDateTime drawDate){
        NumbersValidator numbersValidator = new NumbersValidator();
        TicketRandomUUID uuid = new TicketRandomUUID();
        TicketDrawDate ticketDrawDate = new TicketDrawDate(ticketCurrentDateTime);
        TicketGenerator ticketCreated = new TicketGenerator(uuid, ticketDrawDate);
        return new NumberReceiverFacade(numbersValidator, ticketRepository, ticketCreated, drawDate);
    }
}
