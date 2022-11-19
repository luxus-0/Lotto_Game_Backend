package pl.lotto.resultchecker;

import java.util.Set;

class ResultsCheckerValidator {

    private final static Integer SIZE_MAX = 6;

    public boolean isWinnerNumbers(Set<Integer> inputNumbers) {
        NumberGenerator generateLottoNumbers = new NumberGenerator();
        Set<Integer> lottoNumbers = generateLottoNumbers.generate();
        return inputNumbers.stream()
                .filter(checkNumbers -> inputNumbers.size() <= SIZE_MAX)
                .anyMatch(lottoNumbers::contains);
    }
}
