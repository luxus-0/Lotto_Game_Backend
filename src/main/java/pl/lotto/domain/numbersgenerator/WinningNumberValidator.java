package pl.lotto.domain.numbersgenerator;

import org.springframework.beans.factory.annotation.Value;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;

import java.util.Optional;
import java.util.Set;

public class WinningNumberValidator {
    @Value("${range.from.number}")
    private int RANGE_FROM_NUMBER;
    @Value("${range.to.number}")
    private int RANGE_TO_NUMBER;
    @Value("${validation.message.winning.numbers}")
    private String OUT_OF_RANGE_NUMBERS;

    private String IN_RANGE_NUMBERS = "correct range winning numbers";

    public WinningNumbersDto validate(Set<Integer> winningNumbers) {
        Optional<WinningNumbersDto> winningNumbersDto = winningNumbers.stream()
                .filter(numbers -> numbers < RANGE_FROM_NUMBER)
                .filter(numbers -> numbers > RANGE_TO_NUMBER)
                .map(number -> WinningNumbersDto.builder()
                        .winningNumbers(winningNumbers)
                        .build())
                .findAny();

        if (winningNumbersDto.isPresent()) {
            return WinningNumbersDto.builder()
                    .validationMessage(OUT_OF_RANGE_NUMBERS)
                    .build();
        }
        return WinningNumbersDto.builder()
                .winningNumbers(winningNumbers)
                .validationMessage(IN_RANGE_NUMBERS)
                .build();
    }
}
