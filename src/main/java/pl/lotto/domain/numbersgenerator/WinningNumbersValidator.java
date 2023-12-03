package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pl.lotto.domain.numbersgenerator.exceptions.IncorrectSizeNumbersException;
import pl.lotto.domain.numbersgenerator.exceptions.OutOfRangeNumbersException;

import java.util.Set;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static pl.lotto.domain.numbersgenerator.WinningNumbersValidationMessageProvider.INCORRECT_SIZE;
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
        if(isIncorrectSize(winningNumbers)){
            throw new IncorrectSizeNumbersException(INCORRECT_SIZE);
        }
        return true;
    }

    public void validate(int count, int lowerBand, int upperBand) {
        if (count == 0) {
            throw new ResponseStatusException(NO_CONTENT);
        }
        else if (lowerBand > upperBand && count > properties.count()) {
            throw new ResponseStatusException(NOT_FOUND);
        }
    }

    private boolean outOfRange(Set<Integer> winningNumbers) {
        return winningNumbers.stream()
                .anyMatch(number -> number < properties.lowerBand() || number > properties.upperBand());
    }

    private boolean isIncorrectSize(Set<Integer> winningNumbers){
        return winningNumbers.size() > properties.count();

    }
}
