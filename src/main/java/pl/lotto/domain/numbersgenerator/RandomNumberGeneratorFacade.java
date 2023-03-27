package pl.lotto.domain.numbersgenerator;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.lotto.domain.numbersgenerator.dto.RandomNumberDto;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;
import pl.lotto.domain.numbersgenerator.exception.RandomNumberNotFoundException;
import pl.lotto.infrastructure.numbergenerator.client.NumberGeneratorClient;

import java.util.*;
import java.util.stream.Stream;

@AllArgsConstructor
@Log4j2
@Service
public class RandomNumberGeneratorFacade {

    private final NumberGeneratorClient numberGeneratorClient;
    private final RandomNumberRepository randomNumberRepository;

    public WinningNumbersDto generateSixRandomNumbers() throws JsonProcessingException {
        RandomNumberDto response = numberGeneratorClient.generateSixRandomNumbers();
        if(response != null){
            RandomNumber randomNumber = readRandomNumber(response);
            RandomNumber randomNumberSaved = randomNumberRepository.save(randomNumber);
            return WinningNumbersDto.builder()
                    .winningNumbers(randomNumberSaved.randomNumbers())
                    .build();
        }
        return WinningNumbersDto.builder()
                .winningNumbers(Set.of())
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
