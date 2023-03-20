package pl.lotto.domain.resultchecker;

import java.time.Clock;
import java.time.LocalDateTime;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.temporal.TemporalAdjusters.next;

public class ResultCheckerDateTime {
    private final Clock clock;

    public ResultCheckerDateTime(Clock clock) {
        this.clock = clock;
    }

    public LocalDateTime readDateTimeDraw() {
        return LocalDateTime.now(clock)
                .with(next(SATURDAY))
                .withHour(12)
                .withMinute(0);
    }
}
