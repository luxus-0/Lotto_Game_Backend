package pl.lotto.resultchecker;

import java.util.Set;
import java.util.stream.Collectors;

import static pl.lotto.numberreceiver.NumbersMessageProvider.SIZE_NUMBERS;

class ResultsChecker {

    public boolean checkWinnerNumbers(Set<Integer> inputNumbers, Set<Integer> randomNumbers){
        return inputNumbers.stream().filter(checkNumbers -> valid(randomNumbers)).anyMatch(randomNumbers::contains);
    }

    public Set<Integer> showWinnerNumbers(Set<Integer> inputNumbers, Set<Integer> randomNumbers){
        return inputNumbers.stream().filter(isWinner -> checkWinnerNumbers(inputNumbers, randomNumbers)).collect(Collectors.toSet());
    }

    public boolean valid(Set<Integer> winnerNumbers){
        return winnerNumbers.size() <= SIZE_NUMBERS;
    }
}
