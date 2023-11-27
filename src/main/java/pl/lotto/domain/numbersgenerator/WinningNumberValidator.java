package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.lotto.domain.numbersgenerator.exceptions.OutOfRangeNumbersException;

import java.util.Set;

@AllArgsConstructor
@Log4j2
class WinningNumberValidator {
    private final WinningNumbersConfigurationProperties properties;

    public boolean validate(Set<Integer> winningNumbers) {
        if (outOfRange(winningNumbers)) {
            try {
                throw new OutOfRangeNumbersException("Numbers out of range!");
            } catch (OutOfRangeNumbersException e) {
                log.error(e.getMessage());
            }
        }
        return true;
    }

    private boolean outOfRange(Set<Integer> winningNumbers) {
        return winningNumbers.stream()
                .anyMatch(number -> number < properties.lowerBand() || number > properties.upperBand());
    }
}
