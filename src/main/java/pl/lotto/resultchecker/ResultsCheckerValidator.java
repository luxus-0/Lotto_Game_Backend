package pl.lotto.resultchecker;

import java.util.Set;

class ResultsCheckerValidator {

    private final static Integer SIZE_MAX = 6;
    private final NumberGenerator numberGenerator = new NumberGenerator();

    boolean isWinnerNumbers(Set<Integer> inputNumbers) {
        LottoNumbersGenerator lottoNumbers = numberGenerator.generate();
        return inputNumbers.stream()
                .filter(checkUserNumbers -> inputNumbers.size() <= SIZE_MAX)
                .anyMatch(containingLottoNumbers -> inputNumbers.contains(lottoNumbers.lottoNumber));
    }
}
