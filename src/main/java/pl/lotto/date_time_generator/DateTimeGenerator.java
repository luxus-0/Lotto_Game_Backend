package pl.lotto.date_time_generator;

import java.time.*;

public class DateTimeGenerator {

    private final Clock clock;
    private LocalTime drawTime;
    private DayOfWeek drawDayOfWeek;
    private Duration numberDays;

    public DateTimeGenerator(Clock clock) {
        this.clock = clock;
    }

    public LocalDateTime getCurrentDateAndTime() {
        return LocalDateTime.now(clock);
    }

    public LocalDateTime getDrawDate(LocalDateTime currentDateTime) {
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

    public LocalDateTime getExpirationDateAndTime(LocalDateTime drawDateTime) {
        return drawDateTime.plusDays(numberDays.toDays());
    }
}
