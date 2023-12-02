package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.lotto.domain.numbersgenerator.exceptions.OutOfRangeNumbersException;

import java.util.Set;

import static pl.lotto.domain.numbersgenerator.WinningNumbersValidationMessageProvider.OUT_OF_RANGE;

@AllArgsConstructor
@Log4j2
class WinningNumbersValidator {
    private final WinningNumbersConfigurationProperties properties;

    public boolean validate(Set<Integer> winningNumbers) {
        if (outOfRange(winningNumbers)) {
            try {
                throw new OutOfRangeNumbersException(OUT_OF_RANGE);
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
