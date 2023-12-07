package pl.lotto.domain.numbersgenerator;

import pl.lotto.domain.numbersgenerator.dto.RandomNumbersResponseDto;

public interface RandomNumbersGenerator {
    String generateTicketUUID();

    RandomNumbersResponseDto generateRandomNumbers(int count, int lowerBand, int upperBand);
}
