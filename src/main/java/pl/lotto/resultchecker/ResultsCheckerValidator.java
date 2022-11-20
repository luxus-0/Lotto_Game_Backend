package pl.lotto.resultchecker;

import java.util.Set;

class ResultsCheckerValidator {

    private final static Integer SIZE_MAX = 6;

    public boolean isWinnerNumbers(Set<Integer> inputNumbers) {
        NumberGenerator numberGenerator = new NumberGenerator();
        Set<Integer> lottoNumbers = numberGenerator.generate();
        return inputNumbers.stream()
                .filter(checkNumbers -> inputNumbers.size() <= SIZE_MAX)
                .anyMatch(lottoNumbers::contains);
    }
}
