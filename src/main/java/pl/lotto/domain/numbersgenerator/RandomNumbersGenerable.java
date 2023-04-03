package pl.lotto.domain.numbersgenerator;

import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;
import pl.lotto.infrastructure.numbergenerator.client.QueryParametersUrl;

public interface RandomNumbersGenerable {
    RandomNumbersDto generateRandomNumbers(QueryParametersUrl queryParametersUrl);
}
