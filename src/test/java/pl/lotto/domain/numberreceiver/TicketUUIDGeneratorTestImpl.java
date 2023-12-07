package pl.lotto.domain.numberreceiver;

import java.util.UUID;

public class TicketUUIDGeneratorTestImpl implements TicketUUIDGenerator {

    private final String ticketUUID;

    public TicketUUIDGeneratorTestImpl() {
        ticketUUID = UUID.randomUUID().toString();
    }

    @Override
    public String generateTicketUUID() {
        return ticketUUID;
    }
}
