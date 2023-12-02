package pl.lotto.domain.numberreceiver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.drawdate.AdjustableClock;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.drawdate.DrawDateGenerator;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Configuration
public class NumberReceiverConfiguration {

    @Bean
    AdjustableClock adjustableClock() {
        return new AdjustableClock(LocalDateTime.of(2022, 11, 16, 10, 0, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
    }

    @Bean
    TicketIdGenerator hashGenerable() {
        return new TicketIdGeneratorImpl();
    }

    @Bean
    NumbersReceiverValidator numbersReceiverValidator() {
        return new NumbersReceiverValidator();
    }

    @Bean
    public NumberReceiverFacade numberReceiverFacade(AdjustableClock clock, TicketIdGenerator hashGenerator, TicketRepository ticketRepository) {
        NumbersReceiverValidator numbersReceiverValidator = new NumbersReceiverValidator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        DrawDateFacade drawDateFacade = new DrawDateFacade(drawDateGenerator);
        return new NumberReceiverFacade(numbersReceiverValidator, drawDateFacade, ticketRepository, hashGenerator);
    }
}
