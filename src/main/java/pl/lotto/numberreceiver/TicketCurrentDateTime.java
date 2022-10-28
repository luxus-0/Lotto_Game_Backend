package pl.lotto.numberreceiver;

import java.time.Clock;
import java.time.LocalDateTime;

class TicketCurrentDateTime {
    private final Clock clock;

    TicketCurrentDateTime(Clock clock) {
        this.clock = clock;
    }

    public LocalDateTime generateToday() {
        return LocalDateTime.now(clock);
    }
}
