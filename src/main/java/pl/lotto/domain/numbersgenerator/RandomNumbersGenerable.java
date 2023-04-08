package pl.lotto.domain.numbersgenerator;

import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;
import pl.lotto.infrastructure.numbergenerator.client.RandomNumberParametersUrl;

public interface RandomNumbersGenerable {
    RandomNumbersDto generateRandomNumbers(RandomNumberParametersUrl randomNumberParametersUrl);
}
