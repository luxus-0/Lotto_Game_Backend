package pl.lotto.domain.numbersgenerator;

import org.springframework.beans.factory.annotation.Value;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;

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

    public WinningNumbersDto validate(Set<Integer> winningNumbers) {
        if(!isInRange(winningNumbers)){
            return WinningNumbersDto.builder()
                    .message(OUT_OF_RANGE_NUMBERS)
                    .build();
        }
        else if(!isCorrectSize(winningNumbers)){
            return WinningNumbersDto.builder()
                    .message(INCORRECT_SIZE_NUMBERS)
                    .build();
        }
        return WinningNumbersDto.builder()
                .winningNumbers(winningNumbers)
                .build();
    }

    private boolean isCorrectSize(Set<Integer> winningNumbers) {
        return winningNumbers.size() == QUANTITY_NUMBERS;
    }

    private boolean isInRange(Set<Integer> winningNumbers) {
        return winningNumbers.stream()
                .anyMatch(number -> number >= RANGE_FROM_NUMBER && number <= RANGE_TO_NUMBER);
    }
}
