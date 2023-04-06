package pl.lotto.domain.drawdate;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.LocalTime.NOON;
import static java.time.ZoneOffset.UTC;
import static java.time.temporal.TemporalAdjusters.next;

@Service
@AllArgsConstructor
@Log4j2
public class DrawDateGenerator {
    private final AdjustableClock clock;
    LocalDateTime generateNextDrawDate() {
        LocalDateTime currentDateTime = LocalDateTime.now(clock.withZone(UTC));
        if (isSaturdayAndBeforeNoon(currentDateTime)) {
            throw new NextDrawDateNotFoundException(currentDateTime.toLocalDate() + " " + currentDateTime.toLocalTime() + "\nNext draw date: " +readNextDrawDate());
        } else if (isSaturdayAndAfterNoon(currentDateTime)) {
            throw new NextDrawDateNotFoundException(currentDateTime.toLocalDate() + " " + currentDateTime.toLocalTime() + "\nNext draw date: " +readNextDrawDate());
        } else {
            return readNextDrawDate();
        }
    }

    private boolean isSaturdayAndAfterNoon(LocalDateTime currentDateTime) {
        return currentDateTime.getDayOfWeek().equals(SATURDAY) && currentDateTime.toLocalTime().isAfter(NOON);
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
