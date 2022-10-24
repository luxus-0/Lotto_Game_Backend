package pl.lotto.numberreceiver;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

class DateMessageProvider {
    public static final int YEAR = LocalDate.now().getYear();
    public static final int MONTH = LocalDate.now().getMonth().getValue();
    public static final int DAY = DayOfWeek.SATURDAY.plus(7).getValue();
    public static final int HOUR = 12;
    public static final int MINUTES = 0;
    public static final String MESSAGE_DATE = "DRAW DATE FOR "+ DAY + "AT TIME : "+HOUR + " : " +MINUTES;
    public static final LocalDateTime DRAW_DATE = LocalDateTime.of(YEAR, MONTH, DAY, HOUR, MINUTES);
}
