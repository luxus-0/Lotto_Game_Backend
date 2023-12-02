package pl.lotto.domain.drawdate;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.LocalTime.NOON;
import static java.time.temporal.TemporalAdjusters.next;

@AllArgsConstructor
public class DrawDateGenerator {
    private final AdjustableClock clock;

    LocalDateTime generateNextDrawDate() {
        return LocalDateTime.now(clock)
                .with(next(SATURDAY))
                .with(NOON);
    }
}
