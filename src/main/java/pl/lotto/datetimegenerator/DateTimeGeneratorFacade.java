package pl.lotto.datetimegenerator;

import java.time.LocalDateTime;

public class DateTimeGeneratorFacade {
    private final DateTimeActualGenerator dateTimeActualGenerator;
    private final DateTimeDrawGenerator dateTimeDrawGenerator;
    private final DateTimeExpiryGenerator dateTimeExpiryGenerator;

    public DateTimeGeneratorFacade(DateTimeActualGenerator dateTimeActualGenerator, DateTimeDrawGenerator dateTimeDrawGenerator, DateTimeExpiryGenerator dateTimeExpiryGenerator) {
        this.dateTimeActualGenerator = dateTimeActualGenerator;
        this.dateTimeDrawGenerator = dateTimeDrawGenerator;
        this.dateTimeExpiryGenerator = dateTimeExpiryGenerator;
    }


    public LocalDateTime readActualDateTime() {
    }

    public LocalDateTime readDrawDateTime() {
    }

    public LocalDateTime readExpirationDateTime() {
    }
}
