package pl.lotto.domain.numbersgenerator;

import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;
import pl.lotto.infrastructure.numbergenerator.client.RandomNumberParametersUrl;

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
    public RandomNumbersDto generateRandomNumbers(RandomNumberParametersUrl randomNumberParametersUrl) {
        return RandomNumbersDto.builder()
                .randomNumbers(generatedNumbers)
                .build();
    }
}