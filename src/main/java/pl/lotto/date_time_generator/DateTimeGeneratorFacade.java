package pl.lotto.date_time_generator;

import java.time.LocalDateTime;

public class DateTimeGeneratorFacade {
    private final DateTimeGenerator dateTimeGenerator;

    public DateTimeGeneratorFacade(DateTimeGenerator dateTimeGenerator) {
        this.dateTimeGenerator = dateTimeGenerator;
    }

    public LocalDateTime readActualDateAndTime() {
        return dateTimeGenerator.getCurrentDateAndTime();
    }

    public LocalDateTime readDrawDateAndTime(LocalDateTime date) {
        return dateTimeGenerator.getDrawDate(date);
    }

    public LocalDateTime readExpirationDateAndTime(LocalDateTime date) {
        return dateTimeGenerator.getExpirationDateAndTime(date);
    }
}
