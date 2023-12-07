package pl.lotto.domain.numberreceiver;

import java.util.UUID;

public class TicketUUIDGeneratorImpl implements TicketUUIDGenerator {
    @Override
    public String generateTicketUUID() {
        return UUID.randomUUID().toString();
    }
}
