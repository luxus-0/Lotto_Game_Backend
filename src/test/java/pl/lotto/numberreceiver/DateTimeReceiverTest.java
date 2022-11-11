package pl.lotto.numberreceiver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DateTimeReceiverTest {

    @Test
    @DisplayName("return failed when user gave sunday november 2022 11:00 am with zone Europe/Warsaw")
    public void should_return_failed_when_user_gave_sunday_november_2022_year_and_time_11_am_with_zone_Europe_Warsaw() {
        // given
        LocalDate date = LocalDate.of(2022, 11, DayOfWeek.SUNDAY.getValue());
        LocalTime time = LocalTime.of(11, 0);
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        Clock clock = Clock.fixed(Instant.now(), ZoneId.of("Europe/Warsaw"));
        DateTimeReceiver dateTimeReceiver = new DateTimeReceiver(clock);

        // when
        LocalDateTime drawDate = dateTimeReceiver.generateDrawDate(dateTime);

        // then
        assertNotEquals(drawDate.toLocalTime(), time);
        assertNotEquals(drawDate.toLocalDate(), date);
    }

    @Test
    @DisplayName("return success when user gave Wednesday November 2022 12:00 am with zone Europe/Warsaw")
    public void should_return_success_when_user_gave_Wednesday_November_2022_year_and_time_12_am_with_zone_Europe_Warsaw() {
        // given
        LocalDate date = LocalDate.of(2022, 11, DayOfWeek.SATURDAY.getValue());
        LocalTime time = LocalTime.of(12, 0);
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        Clock clock = Clock.fixed(Instant.now(), ZoneId.of("Europe/Warsaw"));
        DateTimeReceiver dateTimeReceiver = new DateTimeReceiver(clock);

        // when
        LocalDateTime drawDate = dateTimeReceiver.generateDrawDate(dateTime);

        // then
        assertThat(drawDate.getDayOfWeek()).isEqualTo(DayOfWeek.SATURDAY);
        assertEquals(drawDate.toLocalTime(), time);
    }

    @Test
    @DisplayName("return success when user gave Wednesday November 2022 12:00 am with zone Europe/Warsaw")
    public void should_return_success_when_user_gave_Wednesday_November_2022_year_and_time_12_am_with_zone_Europe_Warsaw() {
        // given
        LocalDate date = LocalDate.of(2022, 11, DayOfWeek.SATURDAY.getValue());
        LocalTime time = LocalTime.of(12, 0);
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        Clock clock = Clock.fixed(Instant.now(), ZoneId.of("Europe/Warsaw"));
        DateTimeReceiver dateTimeReceiver = new DateTimeReceiver(clock);

        // when
        LocalDateTime drawDate = dateTimeReceiver.generateDrawDate(dateTime);

        // then
        assertThat(drawDate.getDayOfWeek()).isEqualTo(DayOfWeek.SATURDAY);
        assertEquals(drawDate.toLocalTime(), time);
    }
}