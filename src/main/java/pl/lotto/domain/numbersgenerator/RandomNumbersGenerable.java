package pl.lotto.domain.numbersgenerator;

import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;

public interface RandomNumbersGenerable {
    String generateUniqueTicketId();
    RandomNumbersDto generateSixRandomNumbers();
}
