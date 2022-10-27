package pl.lotto.datetimegenerator;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

public class DateTimeGenerator {
    private Clock clock;
    private DayOfWeek drawDayOfWeek;
    private LocalTime drawHour;
    private Duration expirationInDays;

    public Clock getClock() {
        return clock;
    }

    public DayOfWeek getDrawDayOfWeek() {
        return drawDayOfWeek;
    }

    public LocalTime getDrawHour() {
        return drawHour;
    }

    public Duration getExpirationInDays() {
        return expirationInDays;
    }
}
