package pl.lotto.datetimegenerator;

import java.time.Duration;
import java.time.LocalDateTime;

public class DateTimeExpiryGenerator {

    private final Duration numberDays;

    public DateTimeExpiryGenerator(Duration numberDays) {
        this.numberDays = numberDays;
    }

    public LocalDateTime generateExpirationDateAndTime(LocalDateTime drawDateTime) {
        return drawDateTime.plusDays(numberDays.toDays());
    }
}
