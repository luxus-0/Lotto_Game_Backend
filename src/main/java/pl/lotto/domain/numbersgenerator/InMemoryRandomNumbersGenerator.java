package pl.lotto.domain.numbersgenerator;


import pl.lotto.domain.numbersgenerator.dto.RandomNumbersResponseDto;
import pl.lotto.domain.numbersgenerator.exceptions.RandomNumbersNotFoundException;

import java.util.Set;

public class InMemoryRandomNumbersGenerator implements RandomNumbersGenerator {
    @Override
    public String generateTicketUUID() {
        return "123456";
    }

    @Override
    public RandomNumbersResponseDto generateRandomNumbers(int count, int lowerBand, int upperBand) {
        if(count == 0 || lowerBand < upperBand){
            throw new RandomNumbersNotFoundException();
        }
        return RandomNumbersResponseDto.builder()
                .randomNumbers(Set.of(14,17,23,56,45,90))
                .build();
    }
}
