package pl.lotto.domain.numbersgenerator;

import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;

import java.util.Set;

public class WinningNumberGeneratorTestImpl implements RandomNumbersGenerable {

    private final Set<Integer> generatedNumbers;

    WinningNumberGeneratorTestImpl() {
        generatedNumbers = Set.of(1, 2, 3, 4, 5, 6);
    }

    WinningNumberGeneratorTestImpl(Set<Integer> generatedNumbers) {
        this.generatedNumbers = generatedNumbers;
    }

    @Override
    public RandomNumbersDto generateRandomNumbers(int count, int lowerBand, int upperBand) {
        return RandomNumbersDto.builder()
                .randomNumbers(generatedNumbers)
                .build();
    }
}