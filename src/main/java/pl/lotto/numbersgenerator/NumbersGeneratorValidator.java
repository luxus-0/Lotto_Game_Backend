package pl.lotto.numbersgenerator;

import java.util.Set;

class NumbersGeneratorValidator {

    private static final int SIZE_MAX = 6;

    boolean valid(Set<Integer> lottoNumbers) {
        return lottoNumbers.stream()
                .anyMatch(checkSize -> lottoNumbers.size() <= SIZE_MAX);
    }
}
