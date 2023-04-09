package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
public class WinningNumbersGeneratorFacade {

    private static final String WINNING_NUMBERS_MESSAGE = "Winning numbers not found";
    private final DrawDateFacade drawDateFacade;
    private final RandomNumbersGenerable randomNumbersGenerable;
    private final WinningNumbersRepository winningNumbersRepository;

    private final WinningNumberValidator winningNumberValidator;

    public WinningNumbersDto generateWinningNumbers() {
        LocalDateTime drawDate = drawDateFacade.retrieveNextDrawDate();
        RandomNumbersDto randomNumbers = randomNumbersGenerable.generateRandomNumbers();
        Set<Integer> winningNumbers = randomNumbers.randomNumbers();
        winningNumberValidator.validate(winningNumbers);
        WinningNumbers winningNumbersDocument = WinningNumbers.builder()
                .winningNumbers(winningNumbers)
                .drawDate(drawDate)
                .build();
        WinningNumbers savedNumbers = winningNumbersRepository.save(winningNumbersDocument);
        return WinningNumbersDto.builder()
                .winningNumbers(savedNumbers.winningNumbers())
                .drawDate(savedNumbers.drawDate())
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
