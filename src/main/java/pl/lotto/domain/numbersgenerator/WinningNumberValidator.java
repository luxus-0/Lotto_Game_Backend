package pl.lotto.domain.numbersgenerator;

import org.springframework.beans.factory.annotation.Value;
import pl.lotto.domain.numbersgenerator.exception.IncorrectSizeNumbersException;
import pl.lotto.domain.numbersgenerator.exception.OutOfRangeNumbersException;

import java.util.Set;

public class WinningNumberValidator {
    @Value("${range.from.number}")
    private int RANGE_FROM_NUMBER;
    @Value("${range.to.number}")
    private int RANGE_TO_NUMBER;
    @Value("${incorrect.size.numbers}")
    private String INCORRECT_SIZE_NUMBERS;

    @Value("${out.of.range.numbers}")
    private String OUT_OF_RANGE_NUMBERS;

    @Value("${quantity.numbers}")
    private int QUANTITY_NUMBERS;

    public boolean validate(Set<Integer> winningNumbers) {
        if (outOfRange(winningNumbers)) {
            throw new OutOfRangeNumbersException(OUT_OF_RANGE_NUMBERS);
        }
        else if(notCorrectSize(winningNumbers)){
            throw new IncorrectSizeNumbersException(INCORRECT_SIZE_NUMBERS);
        }
        else {
            return true;
        }
    }

    private boolean notCorrectSize(Set<Integer> winningNumbers) {
        return winningNumbers.size() != QUANTITY_NUMBERS;
    }

    private boolean outOfRange(Set<Integer> winningNumbers) {
        return winningNumbers.stream()
                .anyMatch(number -> number < RANGE_FROM_NUMBER || number > RANGE_TO_NUMBER);
    }
}
