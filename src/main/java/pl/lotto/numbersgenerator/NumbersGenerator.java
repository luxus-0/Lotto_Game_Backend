package pl.lotto.numbersgenerator;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static pl.lotto.numbersgenerator.NumbersGeneratorMapper.RANDOM_NUMBERS;
import static pl.lotto.numbersgenerator.NumbersGeneratorMessageProvider.SIZE_MAX;
import static pl.lotto.numbersgenerator.NumbersGeneratorMessageProvider.SIZE_MIN;

class NumbersGenerator {
    public Set<Integer> generateNumbers() {
        return IntStream.rangeClosed(SIZE_MIN, SIZE_MAX)
                .map(mapper -> RANDOM_NUMBERS)
                .boxed()
                .collect(Collectors.toSet());
    }
}
