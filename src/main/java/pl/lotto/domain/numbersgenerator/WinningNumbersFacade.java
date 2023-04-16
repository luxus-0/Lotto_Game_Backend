package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
public class WinningNumbersFacade {

    private static final String WINNING_NUMBERS_MESSAGE = "Winning numbers not found";
    private final DrawDateFacade drawDateFacade;
    private final RandomNumbersGenerable randomNumbersGenerable;
    private final WinningNumbersRepository winningNumbersRepository;

    private final WinningNumberValidator winningNumberValidator;

    public WinningNumbersDto generateWinningNumbers() {
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        RandomNumbersDto randomNumbers = randomNumbersGenerable.generateSixRandomNumbers();
        Set<Integer> winningNumbers = randomNumbers.randomNumbers();
        winningNumberValidator.validate(winningNumbers);
        WinningNumbers winningNumbersDocument = WinningNumbers.builder()
                .winningNumbers(winningNumbers)
                .drawDate(nextDrawDate)
                .build();
        if (winningNumbersDocument != null) {
            WinningNumbers saved = winningNumbersRepository.save(winningNumbersDocument);
            return WinningNumbersDto.builder()
                    .winningNumbers(saved.winningNumbers())
                    .drawDate(saved.drawDate())
                    .build();
        }
        throw new WinningNumbersNotFoundException(WINNING_NUMBERS_MESSAGE);
    }

    public WinningNumbersDto retrieveWinningNumbersByDate(LocalDateTime drawDate) {
        WinningNumbers numbersByDate = winningNumbersRepository.findNumbersByDrawDate(drawDate).orElseThrow(() -> new WinningNumbersNotFoundException(WINNING_NUMBERS_MESSAGE));
        return WinningNumbersDto.builder()
                .winningNumbers(numbersByDate.winningNumbers())
                .drawDate(numbersByDate.drawDate())
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
