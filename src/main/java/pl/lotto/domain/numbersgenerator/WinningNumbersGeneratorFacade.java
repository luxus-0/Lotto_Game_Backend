package pl.lotto.domain.numbersgenerator;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;
import pl.lotto.domain.numbersgenerator.exception.WinningNumbersNotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
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
        WinningNumbersDto randomNumbers = randomNumberGeneratorFacade.generateSixRandomNumbers();
        Set<Integer> winningNumbers = randomNumbers.winningNumbers();
        if(winningNumbers != null) {
            WinningNumbers winningNumbersCreator = WinningNumbers.builder()
                    .winningNumbers(winningNumbers)
                    .drawDate(drawDate)
                    .build();

            WinningNumbers winningNumbersSaved = winningNumbersRepository.save(winningNumbersCreator);

            return WinningNumbersDto.builder()
                    .winningNumbers(winningNumbersSaved.winningNumbers())
                    .drawDate(winningNumbersSaved.drawDate())
                    .build();
        }
        throw new WinningNumbersNotFoundException("Winning numbers not found");
    }

    WinningNumbersDto retrieveWinningNumbersByDate(LocalDateTime drawDate) {
        WinningNumbers numbersByDate = winningNumbersRepository.findWinningNumbersByDrawDate(drawDate).orElseThrow(() -> new WinningNumbersNotFoundException("Winning numbers not found"));
        return WinningNumbersDto.builder()
                .winningNumbers(numbersByDate.winningNumbers())
                .drawDate(numbersByDate.drawDate())
                .build();
    }

    public boolean areWinningNumbersGeneratedByDate() {
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        return winningNumbersRepository.existsByDrawDate(nextDrawDate);
    }
}
