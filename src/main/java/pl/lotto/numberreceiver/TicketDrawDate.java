package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.dto.DateDrawResultMessageDto;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

class TicketDrawDate {
    private final TicketCurrentDateTime currentDateTime;

    TicketDrawDate(TicketCurrentDateTime currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    DateDrawResultMessageDto generateDrawDate(LocalDateTime drawDate){
        LocalTime timeNow = currentDateTime.generateToday().toLocalTime();
        LocalDate dateNow = currentDateTime.generateToday().toLocalDate();
        if(timeNow.isBefore(drawDate.toLocalTime()) && dateNow.isBefore(drawDate.toLocalDate())){
            long daysBetweenNowAndDrawDate = Duration.between(dateNow, drawDate).toDays();
            long hoursBetweenNowAndDrawDate = Duration.between(timeNow, drawDate.toLocalTime()).toHours();
            long minutesBetweenNowAndDrawDate = Duration.between(timeNow, drawDate.toLocalTime()).toMinutes();
            long secondsBetweenNowAndDrawDate = Duration.between(timeNow, drawDate.toLocalTime()).toSeconds();

            LocalDate days = dateNow.plusDays(daysBetweenNowAndDrawDate);
            LocalTime hours = timeNow.plusHours(hoursBetweenNowAndDrawDate);
            LocalTime minutes = timeNow.plusMinutes(minutesBetweenNowAndDrawDate);
            LocalTime seconds = timeNow.plusSeconds(secondsBetweenNowAndDrawDate);
            LocalTime localTime = LocalTime.parse(hours.getHour() + ":" + minutes.getMinute() + ":" +seconds.getSecond());
            LocalDateTime localDateTime = LocalDateTime.of(days, localTime);
            return new DateDrawResultMessageDto(localDateTime, DrawDateMessageProvider.SUCCESS_GENERATED_DATE_WITH_TIME);
        }
        return new DateDrawResultMessageDto(Optional.of(drawDate).get(), DrawDateMessageProvider.FAILED_GENERATED_DATE_WITH_TIME);
    }
}
