package pl.lotto.domain.resultchecker;

import pl.lotto.domain.numbersgenerator.exceptions.WinningNumbersNotFoundException;

import java.util.Set;

class ResultCheckerValidation {

    private static final int MIN_NUMBERS = 1;
    private static final int MAX_NUMBERS = 99;
    private static final int QUANTITY_NUMBERS = 6;

    boolean validate(Set<Integer> winnerNumbers) {
        if(isInRange(winnerNumbers) && isCorrectSize(winnerNumbers)){
            return true;
        }
        if(winnerNumbers.isEmpty()){
            throw new WinningNumbersNotFoundException("Winning numbers not found");
        }
        return false;
    }

    private boolean isCorrectSize(Set<Integer> winnerNumbers) {
        return winnerNumbers.size() < QUANTITY_NUMBERS;
    }

    private boolean isInRange(Set<Integer> winnerNumbers) {
        return winnerNumbers.stream().anyMatch(number -> number >= MIN_NUMBERS && number <= MAX_NUMBERS);
    }

}
