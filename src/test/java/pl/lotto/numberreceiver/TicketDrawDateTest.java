package pl.lotto.numberreceiver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TicketDrawDateTest {

    private final TicketDrawDate drawDate;
    private final TicketCurrentDateTime currentDateTime;

    TicketDrawDateTest() {
        Clock clock = Clock.systemUTC();
        this.currentDateTime = new TicketCurrentDateTime(clock);
        this.drawDate = new TicketDrawDate(currentDateTime);
    }

    @Test
    @DisplayName("return failed date time draw given date time with clock UTC")
    public void should_return_date_time_draw_when_user_get_date_time_now_with_UTC_clock() {
        //given
        LocalDate date = LocalDate.of(2000, Month.APRIL, DayOfWeek.SATURDAY.getValue());
        LocalTime time = LocalTime.parse("11:00");
        LocalDateTime actualDateTime = LocalDateTime.of(date, time);
        //when
        LocalDateTime drawDateCreated = drawDate.generateDrawDate(currentDateTime.generateToday());
        //then
        assertNotEquals(actualDateTime, drawDateCreated);
    }
}