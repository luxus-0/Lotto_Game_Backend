package pl.lotto.numberreceiver;

import java.time.*;

class TicketDrawDate {
    private final TicketCurrentDateTime currentDateTime;

    TicketDrawDate(TicketCurrentDateTime currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public LocalDateTime generateDrawDate(LocalDateTime drawDateTime) {
        LocalTime timeNow = currentDateTime.generateToday().toLocalTime();
        LocalDate dateNow = currentDateTime.generateToday().toLocalDate();
        DayOfWeek drawDayOfWeek = drawDateTime.getDayOfWeek();
        LocalTime drawTime = drawDateTime.toLocalTime();
        LocalDate drawDate = drawDateTime.toLocalDate();
        if (isDayEqualSaturdayAndTimeEqualNoon(drawDateTime, timeNow, dateNow, drawDayOfWeek, drawTime)) {
            return LocalDateTime.of(drawDate, drawTime);
        }
        throw new DateTimeException(DrawDateMessageProvider.FAILED_GENERATED_DATE_WITH_TIME);
    }

    private static boolean isDayEqualSaturdayAndTimeEqualNoon(LocalDateTime drawDate, LocalTime timeNow, LocalDate dateNow, DayOfWeek drawDayOfWeek, LocalTime drawTime) {
        return drawDayOfWeek == DayOfWeek.SATURDAY &&
                drawTime == LocalTime.NOON &&
                timeNow == LocalTime.NOON &&
                dateNow == drawDate.toLocalDate();
    }
}
