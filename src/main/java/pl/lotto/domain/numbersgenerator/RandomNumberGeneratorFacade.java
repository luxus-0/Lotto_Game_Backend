package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;
import pl.lotto.infrastructure.numbergenerator.client.NumberGeneratorClient;

import java.util.Set;

@AllArgsConstructor
@Log4j2
@Service
public class RandomNumberGeneratorFacade {

    private final NumberGeneratorClient numberGeneratorClient;
    private final WinningNumbersRepository winningNumbersRepository;

    public WinningNumbersDto generateSixRandomNumbers() {
        WinningNumbersDto response = numberGeneratorClient.generateRandomNumbers();
            WinningNumbers winningNumbers = WinningNumbers.builder()
                    .winningNumbers(response.winningNumbers())
                    .build();

            WinningNumbers winningNumbersSaved = winningNumbersRepository.save(winningNumbers);

            return WinningNumbersDto.builder()
                    .winningNumbers(winningNumbersSaved.winningNumbers())
                    .build();
    }
}
