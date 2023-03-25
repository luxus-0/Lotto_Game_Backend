package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import pl.lotto.domain.numbersgenerator.dto.RandomNumberDto;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@AllArgsConstructor
@Log4j2
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
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    private RandomNumber readRandomNumber(RandomNumberDto randomNumbers) {
        return Stream.of(randomNumbers)
                .filter(Objects::nonNull)
                .map(RandomNumbersMapper::toRandomNumber)
                .findAny()
                .orElse(new RandomNumber("00000", Set.of(), RANDOM_NUMBERS_MESSAGE));
    }
}
