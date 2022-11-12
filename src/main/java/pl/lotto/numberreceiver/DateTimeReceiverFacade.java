package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.dto.DateTimeMessageDto;

import java.time.LocalDateTime;

public class DateTimeReceiverFacade {

    private final DateTimeReceiverValidator dateTimeValidator;

    public DateTimeReceiverFacade(DateTimeReceiverValidator dateTimeValidator) {
        this.dateTimeValidator = dateTimeValidator;
    }

    public DateTimeMessageDto generateDateTimeMessage(LocalDateTime dateTime){
        if(dateTimeValidator.isCorrectDateTimeDraw()) {
            return new DateTimeMessageDto(dateTime, "success");
        }
        return new DateTimeMessageDto(dateTime, "failed");
    }
}
