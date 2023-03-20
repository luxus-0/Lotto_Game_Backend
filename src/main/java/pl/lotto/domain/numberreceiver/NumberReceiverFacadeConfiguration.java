package pl.lotto.domain.numberreceiver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class NumberReceiverFacadeConfiguration {
    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    HashGenerable hashGenerable() {
        return new HashGenerator();
    }

    @Bean
    public NumberReceiverFacade createModuleForTests(Clock clock, HashGenerable hashGenerator, TicketRepository ticketRepository) {
        NumbersReceiverValidator numbersReceiverValidator = new NumbersReceiverValidator();
        DateTimeDrawGenerator dateTimeDraw = new DateTimeDrawGenerator(clock);
        return new NumberReceiverFacade(numbersReceiverValidator, dateTimeDraw, ticketRepository, hashGenerator);
    }
}
