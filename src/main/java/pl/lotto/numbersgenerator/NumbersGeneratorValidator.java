package pl.lotto.numbersgenerator;

import java.util.Set;

class NumbersGeneratorValidator {

    private static final int SIZE_MAX = 6;
    NumbersGeneratorFacade numbersGeneratorFacade;

    boolean checkWinnerNumbers(Set<Integer> inputNumbers) {
        Set<Integer> lottoNumbers = numbersGeneratorFacade.generateLottoNumbers();
        return inputNumbers.stream()
                .filter(checkUserNumbers -> inputNumbers.size() <= SIZE_MAX)
                .anyMatch(containingLottoNumbers -> inputNumbers.contains(
                        lottoNumbers.stream()
                        .findAny()
                        .orElse(0)));
    }
}
