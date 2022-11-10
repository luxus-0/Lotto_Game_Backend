package pl.lotto.numberreceiver;

import java.time.*;

import static java.time.LocalTime.NOON;
import static java.time.MonthDay.now;

class DateTimeReceiver {
    private final Clock clock;

    public DateTimeReceiver(Clock clock) {
        this.clock = clock;
    }

    public LocalDateTime generateToday() {
        return LocalDateTime.now(clock);
    }

    public LocalDateTime generateDrawDate(LocalDateTime dateTime) {
        LocalDate date = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();
        int fromActualYear = Year.from(now()).getValue();
        int fromActualMonth = Month.from(now()).getValue();
        int dayDraw = DayOfWeek.SATURDAY.getValue();
        LocalDate dateDraw = LocalDate.of(fromActualYear, fromActualMonth, dayDraw);
        LocalDateTime dateTimeDraw = LocalDateTime.of(dateDraw, NOON);
        if (dateTime.equals(dateTimeDraw)) {
            return LocalDateTime.of(date, time);
        }
        return dateTime;
    }
}
