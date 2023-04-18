package pl.lotto.domain.numbersgenerator;

import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;

import java.util.Set;

public class InMemoryRandomNumberGenerator implements RandomNumbersGenerable {
    @Override
    public RandomNumbersDto generateSixRandomNumbers() {
        return RandomNumbersDto.builder()
                .randomNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .build();
    }
}
