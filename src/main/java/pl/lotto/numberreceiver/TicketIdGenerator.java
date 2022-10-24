package pl.lotto.numberreceiver;

import java.util.UUID;

class TicketIdGenerator {
    static String generateHash() {
        return UUID.randomUUID().toString();
    }
}
