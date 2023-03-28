package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;
import pl.lotto.domain.numbersgenerator.exception.WinningNumbersNotFoundException;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
public class WinningNumbersGeneratorFacade {

    private static final String NUMBERS_MESSAGE_VALIDATOR = "Winning numbers not found";
    private final RandomNumberGeneratorFacade randomNumberGeneratorFacade;
    private final DrawDateFacade drawDateFacade;
    private final WinningNumberValidator winningNumberValidator;
    private final WinningNumbersRepository winningNumbersRepository;

    WinningNumbersDto generateWinningNumbers() {
        LocalDateTime drawDate = drawDateFacade.retrieveNextDrawDate();
        Set<Integer> winningNumbers = randomNumberGeneratorFacade.generateSixRandomNumbers().winningNumbers();
        if (winningNumbers != null && drawDate != null) {
            winningNumberValidator.validate(winningNumbers);
            WinningNumbers winningNumbersCreator = WinningNumbers.builder()
                    .winningNumbers(winningNumbers)
                    .drawDate(drawDate)
                    .build();

            Set<Integer> winningNumbersSaved = winningNumbersRepository.save(winningNumbersCreator).winningNumbers();

            return WinningNumbersDto.builder()
                    .winningNumbers(winningNumbersSaved)
                    .build();
        }
        return WinningNumbersDto.builder()
                .winningNumbers(Set.of())
                .drawDate(drawDate)
                .validationMessage(NUMBERS_MESSAGE_VALIDATOR)
                .build();
    }

    WinningNumbersDto retrieveWinningNumbersByDate(LocalDateTime drawDate) {
        WinningNumbers numbersByDrawDate = winningNumbersRepository.findWinningNumbersByDrawDate(drawDate).orElseThrow(WinningNumbersNotFoundException::new);
        return WinningNumbersDto.builder()
                .winningNumbers(numbersByDrawDate.winningNumbers())
                .drawDate(numbersByDrawDate.drawDate())
                .build();
    }
}
