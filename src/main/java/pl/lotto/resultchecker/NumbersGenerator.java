package pl.lotto.resultchecker;

import java.util.Random;
import java.util.stream.IntStream;

class NumbersGenerator {
    private static final Integer MIN_NUMBER = 1;
    private static final Integer MAX_NUMBER = 99;

    LottoNumbersGenerator generate() {
        return IntStream.rangeClosed(MIN_NUMBER, MAX_NUMBER)
                .map(randomNumbers -> new Random().nextInt())
                .boxed()
                .map(LottoNumbersGenerator::new)
                .findAny()
                .orElseThrow();
    }
}
