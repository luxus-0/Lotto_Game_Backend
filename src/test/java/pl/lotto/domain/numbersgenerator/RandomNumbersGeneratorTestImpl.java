package pl.lotto.domain.numbersgenerator;

import lombok.Getter;
import pl.lotto.domain.numbersgenerator.dto.RandomNumbersResponseDto;
import pl.lotto.domain.numbersgenerator.exceptions.OutOfRangeNumbersException;

import java.util.Set;

@Getter
public class RandomNumbersGeneratorTestImpl implements RandomNumbersGenerator{

    private final Set<Integer> generatedNumbers;
    RandomNumbersGeneratorTestImpl() {
        generatedNumbers = Set.of(10, 20, 30, 40, 50, 60);
    }

    RandomNumbersGeneratorTestImpl(Set<Integer> generatedNumbers) {
        if(isOutOfRange(generatedNumbers)){
            throw new OutOfRangeNumbersException("Numbers out of range");
        }
        this.generatedNumbers = generatedNumbers;
    }
    @Override
    public String generateTicketUUID() {
        return "123456";
    }

    @Override
    public RandomNumbersResponseDto generateRandomNumbers(int count, int lowerBand, int upperBand) {
        return RandomNumbersResponseDto.builder()
                .randomNumbers(Set.of(10,20,30,40,50,60))
                .build();
    }
    private boolean isOutOfRange(Set<Integer> numbers) {
        return numbers.stream().anyMatch(number -> number < 1 || number > 99);
    }

}
