package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;
import pl.lotto.domain.numbersgenerator.exception.WinningNumbersNotFoundException;
import pl.lotto.infrastructure.numbergenerator.client.RandomNumberClient;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class WinningNumbersGeneratorFacade {

    private static final String WINNING_NUMBERS_MESSAGE = "Winning numbers not found";
    private final DrawDateFacade drawDateFacade;
    private final RandomNumberClient randomNumberClient;
    private final WinningNumbersRepository winningNumbersRepository;

    private final WinningNumberValidator winningNumberValidator;

    public WinningNumbersDto generateWinningNumbers() {
        LocalDateTime drawDate = drawDateFacade.retrieveNextDrawDate();
        RandomNumbersDto randomNumbers = randomNumberClient.generateRandomNumbers();
        Set<Integer> winningNumbers = randomNumbers.randomNumbers();
        if (winningNumbers != null) {
            winningNumberValidator.validate(winningNumbers);
            WinningNumbers winningNumbersCreator = WinningNumbers.builder()
                    .winningNumbers(winningNumbers)
                    .build();

            winningNumbersRepository.save(winningNumbersCreator);

            return WinningNumbersDto.builder()
                    .winningNumbers(winningNumbers)
                    .drawDate(drawDate)
                    .build();
        }
        return WinningNumbersDto.builder()
                .winningNumbers(Collections.emptySet())
                .message(WINNING_NUMBERS_MESSAGE)
                .build();
    }

    public WinningNumbersDto retrieveWinningNumbersByDate(LocalDateTime drawDate) {
        Optional<WinningNumbers> numbersByDate = winningNumbersRepository.findWinningNumbersByDrawDate(drawDate);
        Set<Integer> winningNumbers = numbersByDate.map(WinningNumbers::winningNumbers).orElseThrow(() -> new WinningNumbersNotFoundException(WINNING_NUMBERS_MESSAGE));
        return WinningNumbersDto.builder()
                .winningNumbers(winningNumbers)
                .drawDate(drawDate)
                .build();
    }

    public boolean areWinningNumbersGeneratedByDate() {
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        if (winningNumbersRepository.existsByDrawDate(nextDrawDate)) {
            return true;
        }
        throw new WinningNumbersNotFoundException(WINNING_NUMBERS_MESSAGE);
    }
}
