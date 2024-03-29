package pl.lotto.domain.winningnumbers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Set;

import static pl.lotto.domain.winningnumbers.WinningNumbersValidationResult.INCORRECT_SIZE;
import static pl.lotto.domain.winningnumbers.WinningNumbersValidationResult.OUT_OF_RANGE;

@AllArgsConstructor
@Log4j2
class WinningNumbersValidator {
    private final WinningNumbersConfigurationProperties properties;

    public boolean validate(Set<Integer> winningNumbers) {
        if (outOfRange(winningNumbers)) {
           throw new IllegalArgumentException(OUT_OF_RANGE.getMessage());
        }
        if(isIncorrectSize(winningNumbers)){
            throw new IllegalArgumentException(INCORRECT_SIZE.getMessage());
        }
        return true;
    }

    private boolean outOfRange(Set<Integer> winningNumbers) {
        return winningNumbers.stream()
                .anyMatch(number -> number < properties.lowerBand() || number > properties.upperBand());
    }

    private boolean isIncorrectSize(Set<Integer> winningNumbers){
        return winningNumbers.size() > properties.count();

    }
}
