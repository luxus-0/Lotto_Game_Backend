package pl.lotto.numberreceiver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.core.Local;

import java.time.*;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TicketDrawDateTest {

    private final TicketDrawDate drawDate;

    TicketDrawDateTest() {
        Clock clock = Clock.systemUTC();
        TicketCurrentDateTime currentDateTime = new TicketCurrentDateTime(clock);
        this.drawDate = new TicketDrawDate(currentDateTime);

    }

    @Test
    @DisplayName("return success day of week with clock UTC when today is saturday draw date")
    public void should_return_success_day_of_week_with_clock_UTC_when_saturday() {

        //given
        Clock clock = Clock.systemUTC();
        TicketCurrentDateTime currentDateTime = new TicketCurrentDateTime(clock);
        DayOfWeek dayOfWeek = DayOfWeek.SATURDAY;

        //when
        DayOfWeek resultDay = currentDateTime.generateToday().getDayOfWeek();

        //then
        assertNotEquals(dayOfWeek, resultDay);
    }

    @Test
    @DisplayName("return correct draw day time when user give saturday 11 am with clock UTC when draw date today")
    public void should_return_correct_draw_day_time_when_user_get_saturday_12_am_with_clock_UTC_and_not_today_is_draw_date() {

        //given
        LocalDate actualDate = LocalDate.of(2022, Month.NOVEMBER, DayOfWeek.SATURDAY.getValue());
        LocalTime actualTime = LocalTime.NOON;
        LocalDateTime actualDateTime = LocalDateTime.of(actualDate, actualTime);
        actualDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        //when
        LocalDateTime resultDrawDate = drawDate.generateDrawDate(LocalDateTime.now());

        //then
        assertNotEquals(resultDrawDate, actualDateTime);
    }
}