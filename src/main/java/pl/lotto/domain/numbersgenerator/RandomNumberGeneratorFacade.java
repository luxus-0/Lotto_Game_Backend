package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;
import pl.lotto.infrastructure.numbergenerator.client.RandomNumberGeneratorClient;

@AllArgsConstructor
@Log4j2
@Service
public class RandomNumberGeneratorFacade {

    private final RandomNumberGeneratorClient randomNumberGeneratorClient;
    private final WinningNumbersRepository winningNumbersRepository;

    public RandomNumbersDto generateSixRandomNumbers() {
        RandomNumbersDto response = randomNumberGeneratorClient.generateRandomNumbers();
            WinningNumbers winningNumbers = WinningNumbers.builder()
                    .winningNumbers(response.randomNumbers())
                    .build();

            WinningNumbers winningNumbersSaved = winningNumbersRepository.save(winningNumbers);

            return RandomNumbersDto.builder()
                    .randomNumbers(winningNumbersSaved.winningNumbers())
                    .build();
    }
}
