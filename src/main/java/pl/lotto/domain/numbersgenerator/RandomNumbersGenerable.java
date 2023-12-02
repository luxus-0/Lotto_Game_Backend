package pl.lotto.domain.numbersgenerator;

import pl.lotto.domain.numbersgenerator.dto.RandomNumbersResponseDto;

public interface RandomNumbersGenerable {
    String generateUniqueTicketId();
    RandomNumbersResponseDto generateRandomNumbers(int count, int lowerBand, int upperBand);
}
