package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.lotto.domain.numbersgenerator.exceptions.IncorrectSizeNumbersException;
import pl.lotto.domain.numbersgenerator.exceptions.OutOfRangeNumbersException;
import pl.lotto.domain.numbersgenerator.exceptions.WinningNumbersNotFoundException;

import java.util.Set;

import static pl.lotto.domain.numbersgenerator.WinningNumbersValidationResult.*;

@AllArgsConstructor
@Log4j2
class WinningNumbersValidator {
    private final WinningNumbersConfigurationProperties properties;

    public boolean validate(Set<Integer> winningNumbers) {
        if(winningNumbers == null || winningNumbers.isEmpty()){
            return false;
        }
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
