package pl.lotto.numberreceiver;

public class HashGenerator implements HashGenerable {
    private final String hash;

    public HashGenerator(String hash) {
        this.hash = hash;
    }

    public HashGenerator() {
        this.hash = "123456";
    }

    @Override
    public String getHash() {
        return hash;
    }
}
