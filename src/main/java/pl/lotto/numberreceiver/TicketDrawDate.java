package pl.lotto.numberreceiver;

import java.time.*;
import java.util.Optional;

public class TicketDrawDate {
    private final Clock clock;

    TicketDrawDate(Clock clock) {
        this.clock = clock;
    }

    public LocalDateTime generateToday() {
        return LocalDateTime.now(clock);
    }

    public LocalDateTime generateDrawDate(LocalDateTime drawDateTime) {
        LocalTime timeNow = generateToday().toLocalTime();
        LocalDate dateNow = generateToday().toLocalDate();
        DayOfWeek drawDayOfWeek = drawDateTime.getDayOfWeek();
        LocalTime drawTime = drawDateTime.toLocalTime();
        LocalDate drawDate = drawDateTime.toLocalDate();
        if (isDayEqualSaturdayAndTimeEqualNoon(drawDateTime, timeNow, dateNow, drawDayOfWeek, drawTime)) {
            return LocalDateTime.of(drawDate, drawTime);
        }
        return Optional.of(drawDateTime).orElseThrow();
    }

    public boolean isDayEqualSaturdayAndTimeEqualNoon(LocalDateTime drawDate, LocalTime timeNow, LocalDate dateNow, DayOfWeek drawDayOfWeek, LocalTime drawTime) {
        return drawDayOfWeek == DayOfWeek.SATURDAY &&
                drawTime == LocalTime.NOON &&
                timeNow == LocalTime.NOON &&
                dateNow == drawDate.toLocalDate();
    }
}
