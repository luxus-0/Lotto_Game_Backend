package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;
import pl.lotto.domain.numbersgenerator.exception.RandomNumbersNotFoundException;

import java.util.Objects;
import java.util.stream.Stream;

@AllArgsConstructor
@Log4j2
public class RandomNumbersGeneratorFacade {

    private final RandomNumberGeneratorClient randomNumberGeneratorClient;
    private final RandomNumberRepository randomNumberRepository;
    private static final String RANDOM_NUMBERS_RESPONSE = "random numbers not found";

    public RandomNumbersDto generateSixRandomNumbers(){
        ResponseEntity<RandomNumbersDto> response = randomNumberGeneratorClient.generateSixRandomNumbers();
        if(response.getStatusCode().is2xxSuccessful()){
            RandomNumbersDto randomNumbers = response.getBody();
            RandomNumber randomNumber = readRandomNumber(randomNumbers);
            RandomNumber randomNumberSaved = randomNumberRepository.save(randomNumber);
            log.info("UUID: " + randomNumberSaved.uuid() + "random numbers: " +randomNumberSaved.randomNumbers());
            return new RandomNumbersDto(randomNumberSaved.randomNumbers());
        }
        log.info(RANDOM_NUMBERS_RESPONSE);
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    private RandomNumber readRandomNumber(RandomNumbersDto randomNumbersDto) {
        return Stream.of(randomNumbersDto)
                .filter(Objects::nonNull)
                .map(RandomNumbersMapper::toRandomNumber)
                .findAny()
                .orElseThrow(RandomNumbersNotFoundException::new);
    }
}