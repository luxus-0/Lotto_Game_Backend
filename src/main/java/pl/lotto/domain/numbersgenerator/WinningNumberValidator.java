package pl.lotto.domain.numbersgenerator;

import org.springframework.beans.factory.annotation.Value;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;

import java.util.Set;

public class WinningNumberValidator {
    @Value("${range.from.number}")
    private int RANGE_FROM_NUMBER;
    @Value("${range.to.number}")
    private int RANGE_TO_NUMBER;
    @Value("${quantity.numbers}")
    private int QUANTITY_NUMBERS;

    public WinningNumbersDto validate(Set<Integer> winningNumbers) {
        boolean correctQuantityNumbers = isCorrectQuantityNumbers(winningNumbers);
        return winningNumbers.stream()
                .filter(numbers -> numbers < RANGE_FROM_NUMBER)
                .filter(numbers -> numbers > RANGE_TO_NUMBER)
                .filter(numbers -> !correctQuantityNumbers)
                .map(number -> WinningNumbersDto.builder()
                        .winningNumbers(winningNumbers)
                        .build())
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Number out of range"));
    }
    private boolean isCorrectQuantityNumbers(Set<Integer> winningNumbers) {
        return winningNumbers.size() <= QUANTITY_NUMBERS;
    }
}
