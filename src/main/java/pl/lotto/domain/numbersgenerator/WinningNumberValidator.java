package pl.lotto.domain.numbersgenerator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;

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
        if (!isInRange(winningNumbers)) {
            return WinningNumbersDto.builder()
                    .message(OUT_OF_RANGE_NUMBERS)
                    .build();
        } else if (!isCorrectSize(winningNumbers)) {
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
                .anyMatch(number -> number >= MIN_NUMBER && number <= MAX_NUMBER);
    }
}
