package pl.lotto.domain.numbersgenerator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;
import pl.lotto.domain.numbersgenerator.exception.IncorrectSizeNumbersException;
import pl.lotto.domain.numbersgenerator.exception.OutOfRangeNumbersException;

import java.util.Set;

@Service
public class WinningNumberValidator {
    @Value("${min.number}")
    private int MIN_NUMBER;
    @Value("${max.number}")
    private int MAX_NUMBER;
    @Value("${quantity.numbers}")
    private int QUANTITY_NUMBERS;

    @Value("${out.of.range.numbers}")
    private String OUT_OF_RANGE_NUMBERS;
    @Value("${incorrect.size.numbers}")
    private String INCORRECT_SIZE_NUMBERS;

    public WinningNumbersDto validate(Set<Integer> winningNumbers) {
        if (isNotInRange(winningNumbers)) {
            throw new OutOfRangeNumbersException(OUT_OF_RANGE_NUMBERS);
        } else if (isNotCorrectSize(winningNumbers)) {
            throw  new IncorrectSizeNumbersException(INCORRECT_SIZE_NUMBERS);
        }
        return WinningNumbersDto.builder()
                .winningNumbers(winningNumbers)
                .build();
    }

    private boolean isNotCorrectSize(Set<Integer> winningNumbers) {
        return winningNumbers.size() != QUANTITY_NUMBERS;
    }

    private boolean isNotInRange(Set<Integer> winningNumbers) {
        return winningNumbers.stream()
                .anyMatch(number -> number < MIN_NUMBER || number > MAX_NUMBER);
    }
}
