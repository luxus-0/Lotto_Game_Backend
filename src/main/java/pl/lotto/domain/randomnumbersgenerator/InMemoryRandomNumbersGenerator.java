package pl.lotto.domain.randomnumbersgenerator;


import pl.lotto.domain.winningnumbers.dto.RandomNumbersResponseDto;
import pl.lotto.domain.winningnumbers.exceptions.RandomNumbersNotFoundException;

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
                .randomNumbers(Set.of(1,2,3,4,5,6))
                .build();
    }
}
