package pl.lotto.numberreceiver;

import pl.lotto.domain.numberreceiver.HashGenerable;

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
