package pl.lotto.numberreceiver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TicketDrawDateTest {

   private final Clock clock = Clock.systemUTC();
   private final TicketDrawDate drawDate = new TicketDrawDate(clock);

    @Test
    @DisplayName("return correct day draw when user give saturday with clock UTC")
    public void should_return_correct_day_draw_when_user_give_saturday_with_clock_UTC(){

        //given
        Clock clock = Clock.systemUTC();
        TicketDrawDate drawDate = new TicketDrawDate(clock);
        DayOfWeek dayOfWeek = DayOfWeek.SATURDAY;

        //when
        DayOfWeek resultDay = drawDate.generateToday().getDayOfWeek();

        //then
        assertEquals(dayOfWeek, resultDay);
    }

    @Test
    @DisplayName("return not correct date time draw when user give sunday 11 am with clock UTC")
    public void should_return_not_correct_date_time_draw_when_user_get_sunday_11_am_with_clock_UTC() {

        //given
        LocalDate actualDate = LocalDate.of(2022, Month.NOVEMBER, DayOfWeek.SUNDAY.getValue());
        LocalTime actualTime = LocalTime.of(11,0);
        LocalDateTime actualDateTime = LocalDateTime.of(actualDate, actualTime);
        actualDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        //when
        LocalDateTime resultDrawDate = drawDate.generateDrawDate(LocalDateTime.now());

        //then
        assertNotEquals(resultDrawDate, actualDateTime);
    }

    @Test
    @DisplayName("return correct date time when user give saturday 12 december am with clock UTC")
    public void should_return_correct_date_time_draw_when_user_get_saturday_12_00_am_december_with_clock_UTC() {

        //given
        LocalDateTime date = LocalDate.of(2022, Month.DECEMBER, 3).atStartOfDay();
        LocalDateTime dateTime = date.plus(12, ChronoUnit.HOURS).plusMinutes(0);
        dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

        //when
        LocalDateTime dateTimeResult = drawDate.generateDrawDate(now);

        //then
        assertThat(dateTime).isAfter(dateTimeResult);
    }

    @Test
    @DisplayName("return correct date time draw when user give saturday 11 november am with clock UTC")
    public void should_return_correct_date_time_draw_when_user_get_saturday_11_00_am_december_with_clock_fixed() {

        //given
        LocalDateTime date = LocalDate.of(2022, Month.NOVEMBER, 5).atStartOfDay();
        LocalDateTime dateTime = date.plus(11, ChronoUnit.HOURS).plusMinutes(0);
        dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        LocalDateTime now = LocalDateTime.now(Clock.fixed(Instant.now(), ZoneId.systemDefault()));
        //when
        LocalDateTime dateTimeResult = drawDate.generateDrawDate(now);

        //then
        assertThat(dateTime).isBefore(dateTimeResult);
    }

    @Test
    @DisplayName("return correct date time draw when user give saturday november am with clock UTC")
    public void should_return_correct_date_time_draw_when_user_give_saturday_november_with_clock_UTC() {

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

    @Test
    @DisplayName("return correct time draw when time draw at 10 am is before 12 am")
    public void should_return_correct_when_user_give_10_am_clock_UTC() {

        //given
        LocalTime timeDraw = LocalTime.of(10,0);
        LocalTime correctTimeDraw = LocalTime.NOON;
        //when
        boolean resultTimeDraw = timeDraw.isBefore(correctTimeDraw);
        //then
        assertThat(resultTimeDraw).isEqualTo(true);
    }
}