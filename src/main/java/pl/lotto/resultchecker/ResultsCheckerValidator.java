package pl.lotto.resultchecker;

import java.util.Set;

import static pl.lotto.numberreceiver.NumbersReceiverMessageProvider.SIZE_MAX;

class ResultsCheckerValidator {

    public boolean checkWinnerNumbers(Set<Integer> inputNumbers, Set<Integer> randomNumbers) {
        return inputNumbers.stream().filter(checkNumbers -> inputNumbers.size() <= SIZE_MAX).anyMatch(randomNumbers::contains);
    }
}
