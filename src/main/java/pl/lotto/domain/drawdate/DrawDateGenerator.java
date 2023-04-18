package pl.lotto.domain.drawdate;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.LocalTime.NOON;
import static java.time.temporal.TemporalAdjusters.next;

@AllArgsConstructor
@Log4j2
public class DrawDateGenerator {
    private final AdjustableClock clock;

    LocalDateTime generateNextDrawDate() {
        LocalDateTime currentDateTime = LocalDateTime.now(clock);
        if (isSaturdayAndBeforeNoon(currentDateTime)) {
            return LocalDateTime.of(currentDateTime.toLocalDate(), NOON);
        }
            return readNextDrawDate();
    }

    private boolean isSaturdayAndBeforeNoon(LocalDateTime currentDateTime) {
        return currentDateTime.getDayOfWeek().equals(SATURDAY) && currentDateTime.toLocalTime().isBefore(NOON);
    }

    private LocalDateTime readNextDrawDate() {
        return LocalDateTime.now(clock)
                .with(next(SATURDAY))
                .with(NOON);
    }
}
