package pl.lotto.domain.numbersgenerator;

import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;

import java.util.Set;

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
}