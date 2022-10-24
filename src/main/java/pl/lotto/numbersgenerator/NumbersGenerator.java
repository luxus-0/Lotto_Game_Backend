package pl.lotto.numbersgenerator;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static pl.lotto.numbersgenerator.NumbersGeneratorMapper.RANDOM_NUMBERS;
import static pl.lotto.numbersgenerator.NumbersGeneratorMessageProvider.*;

class NumbersGenerator {
    static Set<Integer> generateNumbers() {
        return IntStream.rangeClosed(SIZE_MIN, SIZE_MAX)
                .map(mapper -> RANDOM_NUMBERS)
                .boxed()
                .collect(Collectors.toSet());
    }

    private static int readRandomNumbers() {
        return new Random().nextInt(MIN_NUMBERS, MAX_NUMBERS);
    }
}
