package pl.lotto.numbergenerator;

import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;
import static pl.lotto.numberreceiver.NumbersReceiverMessageProvider.*;
import static pl.lotto.numberreceiver.NumbersReceiverMessageProvider.RANGE_TO_NUMBER;

public class NumberGenerator {
    public Set<Integer> generate() {
        return IntStream.rangeClosed(SIZE_MIN, SIZE_MAX)
                .map(mapToRandom -> readNumbers())
                .boxed()
                .collect(toSet());
    }

    private int readNumbers() {
        return new Random().nextInt(RANGE_FROM_NUMBER, RANGE_TO_NUMBER);
    }
}
