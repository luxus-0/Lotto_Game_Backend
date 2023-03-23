package pl.lotto.domain.numberreceiver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.AdjustableClock;
import wiremock.org.checkerframework.checker.units.qual.A;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Configuration
public class NumberReceiverFacadeConfiguration {

    @Bean
    AdjustableClock adjustableClock(){
        return new AdjustableClock(LocalDateTime.of(2023, 2, 15, 11, 0, 0,0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
    }

    @Bean
    HashGenerable hashGenerable() {
        return new HashGenerator();
    }

    @Bean
    public NumberReceiverFacade createModuleForTests(AdjustableClock clock, HashGenerable hashGenerator, TicketRepository ticketRepository) {
        NumbersReceiverValidator numbersReceiverValidator = new NumbersReceiverValidator();
        DateTimeDrawGenerator dateTimeDrawGenerator = new DateTimeDrawGenerator(clock);
        return new NumberReceiverFacade(numbersReceiverValidator, dateTimeDrawGenerator, ticketRepository, hashGenerator);
    }
}
