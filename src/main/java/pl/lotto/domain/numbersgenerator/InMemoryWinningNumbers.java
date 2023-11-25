package pl.lotto.domain.numbersgenerator;

import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;

import java.util.Set;
import java.util.UUID;

public class InMemoryWinningNumbers implements RandomNumbersGenerable {
    @Override
    public RandomNumbersDto generateSixRandomNumbers() {
        return RandomNumbersDto.builder()
                .randomNumbers(Set.of(1,2,3,4,5,6))
                .build();
    }

    @Override
    public String generateUniqueTicketId() {
        return UUID.randomUUID().toString();
    }
}
