package pl.lotto.domain.drawdate;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.temporal.TemporalAdjusters.next;

@Service
@AllArgsConstructor
public class DrawDateGenerator {
    private final AdjustableClock clock;

    LocalDateTime generateNextDrawDate() {
        return LocalDateTime.now(clock)
                .with(next(SATURDAY))
                .withHour(12)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }
}
