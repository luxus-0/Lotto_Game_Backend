package pl.lotto.domain.numbersgenerator;

import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;
import pl.lotto.domain.numbersgenerator.exceptions.WinnerNumbersNotFoundException;

import java.util.Set;
import java.util.UUID;

public class WinningNumberGeneratorTestImpl implements RandomNumbersGenerable {

    private final Set<Integer> generatedNumbers;

    public WinningNumberGeneratorTestImpl() {
        generatedNumbers = Set.of(1, 2, 3, 4, 5, 6);
    }

    @Override
    public RandomNumbersDto generateSixRandomNumbers() {
        return RandomNumbersDto.builder()
                .randomNumbers(generatedNumbers)
                .build();
    }

    @Override
    public String generateUniqueTicketId() {
        return UUID.randomUUID().toString();
    }

    public WinningNumbersDto generateWinnerNumbers(Set<Integer> inputNumbers) {
       return WinningNumbersDto.builder()
               .winningNumbers(inputNumbers)
               .build();
    }
}