package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.dto.DateDto;

import java.time.LocalDateTime;

import static pl.lotto.numberreceiver.DateMessageProvider.*;

public class GenerateDateDraw {
    DateDto generateDate(){
        LocalDateTime dateDraw = LocalDateTime.of(YEAR, MONTH, DAY, HOUR, MINUTES);
        return new DateDto(dateDraw, MESSAGE);
    }
}
