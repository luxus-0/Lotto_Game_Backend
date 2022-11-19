package pl.lotto.resultchecker;

import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;

class NumberGenerator {
    private static final Integer MIN_NUMBER = 1;
    private static final Integer MAX_NUMBER = 99;

    Set<Integer> generate() {
        return IntStream.rangeClosed(MIN_NUMBER, MAX_NUMBER)
                .map(mapToRandom -> new Random().nextInt())
                .boxed()
                .collect(toSet());
    }
}
