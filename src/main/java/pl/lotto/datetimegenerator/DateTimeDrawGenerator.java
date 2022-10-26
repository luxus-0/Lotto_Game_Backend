package pl.lotto.datetimegenerator;

import java.time.*;

public class DateTimeDrawGenerator {

    private final LocalTime drawTime;
    private final DayOfWeek drawDayOfWeek;

    public DateTimeDrawGenerator(LocalTime drawTime, DayOfWeek drawDayOfWeek) {
        this.drawTime = drawTime;
        this.drawDayOfWeek = drawDayOfWeek;
    }

    public LocalDateTime generateDrawDate(LocalDateTime currentDateTime) {
        LocalTime actualTime = currentDateTime.toLocalTime();
        LocalDate actualDate = currentDateTime.toLocalDate();
        DayOfWeek actualDayOfWeek = currentDateTime.getDayOfWeek();
        Duration numbersDaysToDraw = readDaysToNextDraw(actualTime, actualDayOfWeek);
        LocalDate drawDate = actualDate.plusDays(numbersDaysToDraw.toDays());
        return LocalDateTime.of(drawDate, drawTime);
    }

    private Duration readDaysToNextDraw(LocalTime actualTime, DayOfWeek actualDayOfWeek) {
        Duration daysToNextDraw;
        if (actualDayOfWeek == drawDayOfWeek && actualTime.isAfter(drawTime)) {
            daysToNextDraw = Duration.ofDays(DayOfWeek.values().length);
        } else {
            daysToNextDraw = getDurationBetweenDaysOfWeek(actualDayOfWeek, drawDayOfWeek);
        }
        return daysToNextDraw;
    }

    private Duration getDurationBetweenDaysOfWeek(DayOfWeek actualDayOfWeek, DayOfWeek drawDayOfWeek) {
        int dayDifference = actualDayOfWeek.getValue() - drawDayOfWeek.getValue();
        if (dayDifference < 0) {
            dayDifference = DayOfWeek.values().length + dayDifference;
        }
        return Duration.ofDays(dayDifference);
    }
}
