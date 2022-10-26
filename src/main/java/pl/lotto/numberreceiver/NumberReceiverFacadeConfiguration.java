package pl.lotto.numberreceiver;

import pl.lotto.datetimegenerator.DateTimeActualGenerator;
import pl.lotto.datetimegenerator.DateTimeDrawGenerator;
import pl.lotto.datetimegenerator.DateTimeExpiryGenerator;
import pl.lotto.datetimegenerator.DateTimeGeneratorFacade;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

public class NumberReceiverFacadeConfiguration {
    NumberReceiverFacade createModuleForTests(TicketRepository ticketRepository,
                                              Clock clock,
                                              LocalTime drawTime,
                                              DayOfWeek drawDayOfWeek,
                                              Duration duration){
        NumbersValidator numbersValidator = new NumbersValidator();
        UuidGenerator uuidGenerator = new UuidGenerator();
        DateTimeActualGenerator dateTimeActualGenerator = new DateTimeActualGenerator(clock);
        DateTimeDrawGenerator dateTimeDrawGenerator = new DateTimeDrawGenerator(drawTime, drawDayOfWeek);
        DateTimeExpiryGenerator dateTimeExpiryGenerator = new DateTimeExpiryGenerator(duration);
        DateTimeGeneratorFacade dateTimeGeneratorFacade = new DateTimeGeneratorFacade(dateTimeActualGenerator, dateTimeDrawGenerator, dateTimeExpiryGenerator);
        TicketGenerator ticketCreated = new TicketGenerator(uuidGenerator, dateTimeGeneratorFacade);
        return new NumberReceiverFacade(numbersValidator, ticketRepository, ticketCreated);
    }
}
