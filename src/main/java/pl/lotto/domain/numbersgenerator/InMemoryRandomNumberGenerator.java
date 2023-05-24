package pl.lotto.domain.numbersgenerator;

import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;
import pl.lotto.domain.numbersgenerator.exceptions.WinnerNumbersNotFoundException;

import java.util.Set;

public class InMemoryRandomNumberGenerator implements RandomNumbersGenerable {
    @Override
    public RandomNumbersDto generateSixRandomNumbers() {
        return RandomNumbersDto.builder()
                .randomNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .build();
    }

    @Override
    public String generateUniqueTicketId() {
        return "123456";
    }

    public WinningNumbersDto generateWinnerNumbers(Set<Integer> inputNumbers) {
        Set<Integer> randomNumbers = generateSixRandomNumbers().randomNumbers();
        Integer inputUserNumbers = inputNumbers.stream().findAny().orElseThrow();

        return randomNumbers.stream()
                .filter(isWinnerNumbers -> randomNumbers.contains(inputUserNumbers))
                .map(numbers -> WinningNumbersDto.builder()
                        .winningNumbers(Set.of(numbers))
                        .build())
                .findAny()
                .orElseThrow(() -> new WinnerNumbersNotFoundException("Winnner numbers not found"));
    }
}
