package pl.lotto.resultchecker;

import java.util.Set;

class ResultsCheckerValidator {

    private final static Integer SIZE_MAX = 6;

    boolean isWinnerNumbers(Set<Integer> inputNumbers, Set<Integer> lottoNumbers) {
        return inputNumbers.stream()
                .filter(checkUserNumbers -> inputNumbers.size() <= SIZE_MAX)
                .filter(checkLottoNumbers -> lottoNumbers.size() <= SIZE_MAX)
                .anyMatch(inputNumbers::contains);
    }
}
