package pl.lotto.resultchecker;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
class ResultsCheckerValidator {

    private static final int SIZE_MAX = 6;
    private final NumbersGenerator numbersGenerator;

    ResultsCheckerValidator(NumbersGenerator numbersGenerator) {
        this.numbersGenerator = numbersGenerator;
    }

    boolean isWinnerNumbers(Set<Integer> inputNumbers) {
        LottoNumbersGenerator lottoNumbers = numbersGenerator.generate();
        return inputNumbers.stream()
                .filter(checkUserNumbers -> inputNumbers.size() <= SIZE_MAX)
                .anyMatch(containingLottoNumbers -> inputNumbers.contains(lottoNumbers.lottoNumber));
    }
}
