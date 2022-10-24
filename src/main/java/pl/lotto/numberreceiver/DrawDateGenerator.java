package pl.lotto.numberreceiver;

import java.time.LocalDateTime;

import static pl.lotto.numberreceiver.DateMessageProvider.*;

public class DrawDateGenerator {
    static LocalDateTime generateDrawDate() {
        return LocalDateTime.of(YEAR, MONTH, DAY, HOUR, MINUTES);
    }
}
