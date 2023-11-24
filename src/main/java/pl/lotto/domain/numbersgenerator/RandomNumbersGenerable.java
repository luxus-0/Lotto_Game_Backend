package pl.lotto.domain.numbersgenerator;

import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;

public interface RandomNumbersGenerable {
    RandomNumbersDto generateSixRandomNumbers();
    String generateUniqueTicketId();

}
