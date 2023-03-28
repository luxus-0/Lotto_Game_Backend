package pl.lotto.domain.drawdate;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.temporal.TemporalAdjusters.next;

@Service
@AllArgsConstructor
@Log4j2
public class DrawDateGenerator {
    private final AdjustableClock clock;
    private final LocalTime DRAW_TIME = LocalTime.of(12,0,0,0);

    LocalDateTime generateNextDrawDate() {
       LocalDateTime currentDateTime = LocalDateTime.now(clock);
        if(isSaturdayAndBeforeNoon(currentDateTime)) {
            log.error("Draw drawDate is saturday and before 12 pm");
            return LocalDateTime.of(currentDateTime.toLocalDate(), DRAW_TIME);
        } else if(isSaturdayAndAfterNoon(currentDateTime)){
            log.error("Draw drawDate is saturday and after 12 pm");
            return LocalDateTime.of(currentDateTime.toLocalDate(), DRAW_TIME);
        }
        return readNextDrawDate();
    }

    private boolean isSaturdayAndAfterNoon(LocalDateTime currentDateTime) {
        return currentDateTime.getDayOfWeek().equals(SATURDAY) && currentDateTime.toLocalTime().isAfter(DRAW_TIME);
    }

    private boolean isSaturdayAndBeforeNoon(LocalDateTime currentDateTime) {
        return currentDateTime.getDayOfWeek().equals(SATURDAY) && currentDateTime.toLocalTime().isBefore(LocalTime.of(12, 0, 0, 0));
    }

    private LocalDateTime readNextDrawDate() {
        return LocalDateTime.now(clock)
                .with(next(SATURDAY))
                .withHour(12)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }
}
