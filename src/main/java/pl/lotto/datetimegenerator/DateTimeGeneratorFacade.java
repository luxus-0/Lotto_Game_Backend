package pl.lotto.datetimegenerator;

import java.time.LocalDateTime;

public class DateTimeGeneratorFacade {

    private final LocalDateTime drawDate;

    public DateTimeGeneratorFacade(LocalDateTime drawDate) {
        this.drawDate = drawDate;
    }
}
