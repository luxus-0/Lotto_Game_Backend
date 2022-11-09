package pl.lotto.resultchecker;

import pl.lotto.numbergenerator.GenerateLottoNumbers;

import java.util.Set;

import static pl.lotto.numberreceiver.NumbersReceiverMessageProvider.SIZE_MAX;

class ResultsCheckerValidator {

    public boolean isWinnerNumbers(Set<Integer> inputNumbers) {
        GenerateLottoNumbers generateLottoNumbers = new GenerateLottoNumbers();
        Set<Integer> lottoNumbers = generateLottoNumbers.generate();
        return inputNumbers.stream().filter(checkNumbers -> inputNumbers.size() <= SIZE_MAX).anyMatch(lottoNumbers::contains);
    }
}
