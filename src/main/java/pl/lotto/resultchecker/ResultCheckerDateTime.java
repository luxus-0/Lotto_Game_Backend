package pl.lotto.resultchecker;

import java.time.Clock;
import java.time.LocalDateTime;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.temporal.TemporalAdjusters.next;

class ResultCheckerDateTime {
    private final Clock clock;

    ResultCheckerDateTime(Clock clock) {
        this.clock = clock;
    }

    public LocalDateTime readDateTimeDraw() {
        return LocalDateTime.now(clock)
                .with(next(SATURDAY))
                .withHour(12)
                .withMinute(0);
    }
}
