package pl.lotto.domain.numberreceiver;

public class HashGeneratorTestImpl implements TicketIdGenerator {

    private final String hash;

    public HashGeneratorTestImpl() {
        hash = "1234567";
    }

    @Override
    public String generateTicketId() {
        return hash;
    }
}
