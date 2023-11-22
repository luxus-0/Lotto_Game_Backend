package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;

import java.time.LocalDateTime;
import java.util.Set;

import static pl.lotto.domain.numbersgenerator.WinningNumbersMessageProvider.WINNING_NUMBERS_NOT_FOUND;

@AllArgsConstructor
@Log4j2
public class WinningNumbersFacade {
    private final DrawDateFacade drawDateFacade;
    private final RandomNumbersGenerable randomNumbersGenerable;
    private final WinningNumbersRepository winningNumbersRepository;
    private final WinningNumberValidator winningNumberValidator;

    public WinningNumbersDto generateWinningNumbers() {
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        RandomNumbersDto sixRandomNumbers = randomNumbersGenerable.generateSixRandomNumbers();
        WinningNumbersDto winningNumbersDto = randomNumbersGenerable.generateWinnerNumbers(sixRandomNumbers.randomNumbers());
        Set<Integer> winningNumbers = winningNumbersDto.winningNumbers();
        String ticketId = randomNumbersGenerable.generateUniqueTicketId();
        winningNumberValidator.validate(winningNumbers);
        WinningNumbers winningNumbersDocument = WinningNumbers.builder()
                .ticketId(ticketId)
                .winningNumbers(winningNumbers)
                .drawDate(nextDrawDate)
                .build();

        WinningNumbers saved = winningNumbersRepository.save(winningNumbersDocument);

        return WinningNumbersDto.builder()
                .ticketId(saved.ticketId())
                .winningNumbers(saved.winningNumbers())
                .drawDate(saved.drawDate())
                .build();
    }

    public WinningNumbersDto retrieveWinningNumbersByDate(LocalDateTime drawDate) {
        return winningNumbersRepository.findWinningNumbersByDrawDate(drawDate).stream()
                .map(winningNumbers -> WinningNumbersDto.builder()
                        .ticketId(winningNumbers.ticketId())
                        .drawDate(winningNumbers.drawDate())
                        .winningNumbers(winningNumbers.winningNumbers())
                .build())
                .findAny()
                .orElseThrow(() -> new WinningNumbersNotFoundException(WINNING_NUMBERS_NOT_FOUND));
    }

    public boolean areWinningNumbersGeneratedByDate() {
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        if (winningNumbersRepository.existsByDrawDate(nextDrawDate)) {
            return true;
        }
        throw new WinningNumbersNotFoundException(WINNING_NUMBERS_NOT_FOUND);
    }
}
