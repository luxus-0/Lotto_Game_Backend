package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.server.ResponseStatusException;
import pl.lotto.domain.numbersgenerator.exceptions.IncorrectSizeNumbersException;
import pl.lotto.domain.numbersgenerator.exceptions.OutOfRangeNumbersException;
import pl.lotto.domain.numbersgenerator.exceptions.WinningNumbersNotFoundException;

import java.util.Set;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static pl.lotto.domain.numbersgenerator.WinningNumbersValidationResult.*;

@AllArgsConstructor
@Log4j2
class WinningNumbersValidator {
    private final WinningNumbersConfigurationProperties properties;

    public boolean validate(Set<Integer> winningNumbers) {
        if(winningNumbers == null){
            throw new WinningNumbersNotFoundException(WINNING_NUMBERS_NOT_FOUND.getMessage());
        }
        if (outOfRange(winningNumbers)) {
            try {
                throw new OutOfRangeNumbersException(OUT_OF_RANGE.getMessage());
            } catch (OutOfRangeNumbersException e) {
                log.error(e.getMessage());
            }
        }
        if(isIncorrectSize(winningNumbers)){
            throw new IncorrectSizeNumbersException(INCORRECT_SIZE.getMessage());
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
