package pl.lotto.numberreceiver;

import java.time.*;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.LocalTime.NOON;
import static java.time.MonthDay.now;

class DateTimeReceiverValidator {
    private final Clock clock;

    public DateTimeReceiverValidator(Clock clock) {
        this.clock = clock;
    }

    public boolean isCorrectDateTimeDraw() {
        int actualYear = Year.now(clock).getValue();
        int actualMonth = Month.from(now(clock)).getValue();
        int actualDay = LocalDateTime.now().getDayOfWeek().getValue();
        LocalTime actualTime = LocalTime.now();
        LocalDate actualDate = LocalDate.of(actualYear, actualMonth, actualDay);
        LocalDateTime dateTimeDraw = LocalDateTime.of(actualDate, NOON);
        if (actualDay == SATURDAY.getValue() && actualTime == NOON) {
            System.out.println(dateTimeDraw);
            return true;
        }
        return false;
    }
}
