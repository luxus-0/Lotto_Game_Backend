package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
    private final WinningNumbersRepository winningNumbersRepository;

    public WinningNumbersDto generateSixRandomNumbers() {
        WinningNumbersDto response = numberGeneratorClient.generateSixRandomNumbers();
        if(response != null){
            WinningNumbers winningNumbers = WinningNumbers.builder()
                    .winningNumbers(response.winningNumbers())
                    .build();

            WinningNumbers winningNumbersSaved = winningNumbersRepository.save(winningNumbers);

            return WinningNumbersDto.builder()
                    .winningNumbers(winningNumbersSaved.winningNumbers())
                    .build();
        }
        throw new IllegalStateException();
    }

    public RandomNumberDto retrieveRandomNumbersByHash(String hash) {
        WinningNumbers winningNumbers = winningNumbersRepository.findWinningNumbersByHash(hash);
            return Set.of(winningNumbers)
                    .stream()
                    .map(dto -> new RandomNumberDto(dto.winningNumbers()))
                    .findAny()
                    .orElseGet(() -> RandomNumberDto.builder()
                            .randomNumbers(Collections.emptySet())
                            .build());
    }
}
