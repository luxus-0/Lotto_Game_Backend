package pl.lotto.domain.drawdate;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.ZoneOffset.UTC;
import static java.time.temporal.TemporalAdjusters.next;

@Service
@AllArgsConstructor
@Log4j2
public class DrawDateGenerator {
    public static final LocalTime DRAW_TIME = LocalTime.of(12, 0, 0, 0);
    private final AdjustableClock clock;

    LocalDateTime generateNextDrawDate() {
        LocalDateTime currentDateTime = LocalDateTime.now(clock.withZone(UTC));
        if (isSaturdayAndBeforeNoon(currentDateTime)) {
            throw new NextDrawDateNotFoundException("Date: \n" +currentDateTime.toLocalDate() + " " + currentDateTime.toLocalTime() + "Next draw date: " +readNextDrawDate());
        } else if (isSaturdayAndAfterNoon(currentDateTime)) {
            throw new NextDrawDateNotFoundException("Date: \n" +currentDateTime.toLocalDate() + " " + currentDateTime.toLocalTime() + "Next draw date: " +readNextDrawDate());
        } else {
            return readNextDrawDate();
        }
    }

    private boolean isSaturdayAndAfterNoon(LocalDateTime currentDateTime) {
        return currentDateTime.getDayOfWeek().equals(SATURDAY) && currentDateTime.toLocalTime().isAfter(DRAW_TIME);
    }

    private boolean isSaturdayAndBeforeNoon(LocalDateTime currentDateTime) {
        return currentDateTime.getDayOfWeek().equals(SATURDAY) && currentDateTime.toLocalTime().isBefore(DRAW_TIME);
    }

    private LocalDateTime readNextDrawDate() {
        return LocalDateTime.now(clock)
                .with(next(SATURDAY))
                .withHour(12)
                .withMinute(0)
                .withSecond(0);
    }
}
