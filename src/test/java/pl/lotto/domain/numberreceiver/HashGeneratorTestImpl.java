package pl.lotto.domain.numberreceiver;

public class HashGeneratorTestImpl implements HashGenerable {

    private final String hash;

    public HashGeneratorTestImpl() {
        hash = "1234567";
    }

    @Override
    public String getHash() {
        return hash;
    }
}
