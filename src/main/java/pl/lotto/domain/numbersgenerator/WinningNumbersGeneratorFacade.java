package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;
import pl.lotto.domain.numbersgenerator.exception.WinningNumbersNotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
public class WinningNumbersGeneratorFacade {

    private static final String NUMBERS_MESSAGE_VALIDATOR = "Winning numbers not found";
    private final RandomNumberGeneratorFacade randomNumberGeneratorFacade;
    private final DrawDateFacade drawDateFacade;
    private final WinningNumbersRepository winningNumbersRepository;

    private final WinningNumberValidator winningNumberValidator;

    public WinningNumbersDto generateWinningNumbers() {
        LocalDateTime drawDate = drawDateFacade.retrieveNextDrawDate();
        WinningNumbersDto randomNumbers = randomNumberGeneratorFacade.generateSixRandomNumbers();
        Set<Integer> winningNumbers = randomNumbers.winningNumbers();
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
                .validationMessage(NUMBERS_MESSAGE_VALIDATOR)
                .build();
    }

    public WinningNumbersDto retrieveWinningNumbersByDate(LocalDateTime drawDate) {
        Optional<WinningNumbers> numbersByDate = winningNumbersRepository.findWinningNumbersByDrawDate(drawDate);
        Set<Integer> winningNumbers = numbersByDate.map(WinningNumbers::winningNumbers).orElseThrow(() -> new WinningNumbersNotFoundException(NUMBERS_MESSAGE_VALIDATOR));
        return WinningNumbersDto.builder()
                .winningNumbers(winningNumbers)
                .drawDate(drawDate)
                .build();
    }

    public boolean areWinningNumbersGeneratedByDate() {
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        return winningNumbersRepository.existsByDrawDate(nextDrawDate);
    }
}
