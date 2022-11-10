package pl.lotto.numberreceiver;

import java.time.*;
import java.util.Optional;

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
        int actualYear = Year.now().getValue();
        int actualMonth = Month.from(now()).getValue();
        int day = DayOfWeek.SATURDAY.getValue();
        LocalDate dateDraw = LocalDate.of(actualYear, actualMonth, day);
        LocalDateTime dateTimeDraw = LocalDateTime.of(dateDraw, NOON);
        if (dateTime.equals(dateTimeDraw)) {
            return dateTimeDraw;
        }
        return Optional.of(dateTimeDraw).get();
    }
}
