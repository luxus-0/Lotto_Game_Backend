package pl.lotto.datetimegenerator;

import java.time.Clock;
import java.time.LocalDateTime;

class CurrentDateTime {
    private final Clock clock;

    CurrentDateTime(Clock clock) {
        this.clock = clock;
    }

    public LocalDateTime generateToday() {
        return LocalDateTime.now(clock);
    }
}
