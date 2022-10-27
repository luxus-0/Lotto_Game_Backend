package pl.lotto.datetimegenerator;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DateTimeGeneratorFacadeConfiguration {
    DateTimeGeneratorFacade createModuleForTests(DateTimeGenerator timeGenerator){
        DateTimeActualGenerator dateTimeActualGenerator = new DateTimeActualGenerator(timeGenerator.getClock());
        DateTimeDrawGenerator dateTimeDrawGenerator = new DateTimeDrawGenerator(timeGenerator.getDrawHour(), timeGenerator.getDrawDayOfWeek());
        DateTimeExpiryGenerator dateTimeExpiryGenerator = new DateTimeExpiryGenerator(timeGenerator.getExpirationInDays());
        return new DateTimeGeneratorFacade(dateTimeActualGenerator, dateTimeDrawGenerator, dateTimeExpiryGenerator);
    }
}
