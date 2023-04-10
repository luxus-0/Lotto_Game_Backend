package pl.lotto.domain.numbersgenerator;

import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;

public interface RandomNumbersGenerable {
    RandomNumbersDto generateRandomNumbers(int count, int lowerBand, int upperBand);
}
