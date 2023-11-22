package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numbersgenerator.exceptions.OutOfRangeNumbersException;

import java.util.Set;

@AllArgsConstructor
class WinningNumberValidator {
    private final WinningNumbersConfigurationProperties properties;

    public void validate(Set<Integer> winningNumbers) {
        if (outOfRange(winningNumbers)) {
            throw new OutOfRangeNumbersException("Numbers out of range!");
        }
    }

    private boolean outOfRange(Set<Integer> winningNumbers) {
        return winningNumbers.stream()
                .anyMatch(number -> number < properties.lowerBand() || number > properties.upperBand());
    }
}
