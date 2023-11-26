package pl.lotto.domain.numberreceiver;

import java.util.UUID;

public class HashGeneratorTestImpl implements TicketIdGenerator {

    private final String hash;

    public HashGeneratorTestImpl() {
        hash = UUID.randomUUID().toString();
    }

    @Override
    public String generateTicketId() {
        return hash;
    }
}
