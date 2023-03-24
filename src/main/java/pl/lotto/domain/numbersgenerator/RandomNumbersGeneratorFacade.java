package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import pl.lotto.domain.numbersgenerator.dto.RandomNumberDto;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@AllArgsConstructor
public class RandomNumbersGeneratorFacade {

    private final RandomNumberGeneratorClient randomNumberGeneratorClient;
    private final RandomNumberRepository randomNumberRepository;
    private static final String RANDOM_NUMBERS_MESSAGE = "random numbers not found";

    public RandomNumberDto generateSixRandomNumbers(){
        ResponseEntity<RandomNumberDto> sixRandomNumbers = randomNumberGeneratorClient.generateSixRandomNumbers();
        if(sixRandomNumbers.getStatusCode().is2xxSuccessful()){
            RandomNumberDto randomNumbers = sixRandomNumbers.getBody();
            RandomNumber randomNumber = readRandomNumber(randomNumbers);
            RandomNumber randomNumberSaved = randomNumberRepository.save(randomNumber);
            return new RandomNumberDto(randomNumberSaved.uuid(), randomNumberSaved.randomNumbers());
        }
        return new RandomNumberDto("", Set.of());
    }

    private RandomNumber readRandomNumber(RandomNumberDto randomNumbers) {
        return Stream.of(randomNumbers)
                .filter(Objects::nonNull)
                .map(RandomNumbersMapper::toRandomNumber)
                .findAny()
                .orElse(new RandomNumber("00000", Set.of(), RANDOM_NUMBERS_MESSAGE));
    }
}
