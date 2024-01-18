package pl.lotto.domain.randomnumbersgenerator;

import pl.lotto.domain.winningnumbers.dto.RandomNumbersResponseDto;

public interface RandomNumbersGenerator {
    String generateTicketUUID();

    RandomNumbersResponseDto generateRandomNumbers(int count, int lowerBand, int upperBand);
}
