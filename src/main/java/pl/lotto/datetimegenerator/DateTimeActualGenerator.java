package pl.lotto.datetimegenerator;

import java.time.Clock;
import java.time.LocalDateTime;

public class DateTimeActualGenerator {

    private final Clock clock;

    public DateTimeActualGenerator(Clock clock) {
        this.clock = clock;
    }

    public LocalDateTime generateCurrentDateAndTime() {
        return LocalDateTime.now(clock);
    }
}
