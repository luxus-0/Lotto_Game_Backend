package pl.lotto.numberreceiver;

import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.LocalDateTime;

@Configuration
public class NumberReceiverFacadeConfiguration {

    NumberReceiverFacade createModuleForTests(TicketRepository ticketRepository, LocalDateTime drawDate){
        Clock clock = Clock.systemUTC();
        NumbersValidator numbersValidator = new NumbersValidator();
        TicketRandomUUID uuid = new TicketRandomUUID();
        TicketDrawDate ticketDrawDate = new TicketDrawDate(clock);
        TicketGenerator ticketCreated = new TicketGenerator(uuid, ticketDrawDate);
        return new NumberReceiverFacade(numbersValidator, ticketRepository, ticketCreated, drawDate);
    }
}
