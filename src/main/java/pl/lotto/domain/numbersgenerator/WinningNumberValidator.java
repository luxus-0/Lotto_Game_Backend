package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numbersgenerator.exceptions.IncorrectSizeNumbersException;
import pl.lotto.domain.numbersgenerator.exceptions.OutOfRangeNumbersException;

import java.util.Set;

@AllArgsConstructor
class WinningNumberValidator {
    private final WinningNumbersFacadeConfigurationProperties properties;

    public Set<Integer> validate(Set<Integer> winningNumbers) {
        if (!inRange(winningNumbers)) {
            throw new OutOfRangeNumbersException("Numbers out of range");
        }
        if (isNotCorrectSize(winningNumbers)) {
            throw new IncorrectSizeNumbersException("Incorrect size numbers");
        }
        return winningNumbers;
    }

    private boolean isNotCorrectSize(Set<Integer> winningNumbers) {
        return winningNumbers.size() < properties.parametersUrl().count();
    }

    private boolean inRange(Set<Integer> winningNumbers) {
        return winningNumbers.stream()
                .anyMatch(number -> number >= properties.parametersUrl().lowerBand() && number <= properties.parametersUrl().upperBand());
    }
}
