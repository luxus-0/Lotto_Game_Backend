package pl.lotto.domain.numberreceiver;

import pl.lotto.numberreceiver.HashGenerable;

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
