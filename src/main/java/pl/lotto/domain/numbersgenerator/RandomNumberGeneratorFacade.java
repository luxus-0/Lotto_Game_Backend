package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import pl.lotto.domain.numbersgenerator.dto.RandomNumberDto;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;
import pl.lotto.domain.numbersgenerator.exception.RandomNumberNotFoundException;
import pl.lotto.infrastructure.numbergenerator.client.NumberGeneratorClient;

import java.util.*;
import java.util.stream.Stream;

@AllArgsConstructor
@Log4j2
public class RandomNumberGeneratorFacade {

    private final NumberGeneratorClient numberGeneratorClient;
    private final RandomNumberRepository randomNumberRepository;

    public WinningNumbersDto generateSixRandomNumbers(){
        ResponseEntity<RandomNumberDto> response = numberGeneratorClient.generateSixRandomNumbers();
        if(response.getStatusCode().is2xxSuccessful()){
            RandomNumberDto randomNumbers = response.getBody();
            RandomNumber randomNumber = readRandomNumber(randomNumbers);
            RandomNumber randomNumberSaved = randomNumberRepository.save(randomNumber);
            log.info("UUID: " + randomNumberSaved.uuid() + "random numbers: " +randomNumberSaved.randomNumbers());
            return WinningNumbersDto.builder()
                    .winningNumbers(randomNumberSaved.randomNumbers())
                    .build();
        }
        log.info("Status code: " + response.getStatusCode());
        return WinningNumbersDto.builder()
                .winningNumbers(Collections.emptySet())
                .build();
    }

    public RandomNumberDto retrieveRandomNumbersByHash(String hash) {
        List<RandomNumber> randomNumbers = randomNumberRepository.findRandomNumberByUuid(hash);
        if (randomNumbers != null) {
            return randomNumbers.stream()
                    .map(dto -> new RandomNumberDto(dto.randomNumbers()))
                    .findAny()
                    .orElseGet(() -> RandomNumberDto.builder()
                            .randomNumbers(Collections.emptySet())
                            .build());
        }
        return new RandomNumberDto(Set.of());
    }

    private RandomNumber readRandomNumber(RandomNumberDto randomNumberDto) {
        return Stream.of(randomNumberDto)
                .filter(Objects::nonNull)
                .map(RandomNumberMapper::toRandomNumber)
                .findAny()
                .orElseThrow(RandomNumberNotFoundException::new);
    }
}
