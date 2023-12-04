package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numbersgenerator.dto.RandomNumbersResponseDto;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketResponseDto;
import pl.lotto.domain.numbersgenerator.exceptions.WinningNumbersNotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import static pl.lotto.domain.numbersgenerator.RandomNumbersUrlMessage.*;
import static pl.lotto.domain.numbersgenerator.WinningNumbersManager.retrieveWinningNumbers;
import static pl.lotto.domain.numbersgenerator.WinningNumbersValidationMessageProvider.NO_WINNING_TICKET;
import static pl.lotto.domain.numbersgenerator.WinningNumbersValidationMessageProvider.WINNING_NUMBERS_NOT_FOUND;

@AllArgsConstructor
@Log4j2
@Builder
public class WinningNumbersFacade {
    private final DrawDateFacade drawDateFacade;
    private final WinningNumbersRepository winningNumbersRepository;
    private final WinningNumbersValidator winningNumbersValidator;
    private final NumberReceiverFacade numberReceiverFacade;
    private final RandomNumbersGenerable randomNumbersGenerable;
    private final WinningNumbersConfigurationProperties properties;

    public WinningTicketResponseDto generateWinningNumbers() {
        String ticketId = randomNumbersGenerable.generateUniqueTicketId();
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        RandomNumbersResponseDto randomNumbersResponse = randomNumbersGenerable.generateRandomNumbers(COUNT_RANDOM_NUMBERS, LOWER_BAND_RANDOM_NUMBERS, UPPER_BAND_RANDOM_NUMBERS);
        Set<Integer> randomNumbers = randomNumbersResponse.randomNumbers();
        Set<Integer> userInputNumbers = numberReceiverFacade.retrieveUserNumbersByDrawDate(nextDrawDate);
        Set<Integer> winningNumbers = retrieveWinningNumbers(randomNumbers, userInputNumbers);
        boolean validate = winningNumbersValidator.validate(winningNumbers);
        if (validate) {
            WinningNumbers winningTicket = new WinningNumbers(ticketId, winningNumbers, nextDrawDate);
            WinningNumbers savedWinningNumbers = winningNumbersRepository.save(winningTicket);
            log.info("Winning ticket: " + savedWinningNumbers);

            return WinningTicketResponseDto.builder()
                    .winningNumbers(savedWinningNumbers.winningNumbers())
                    .drawDate(savedWinningNumbers.drawDate())
                    .build();
        }
        return WinningTicketResponseDto.builder()
                .winningNumbers(Collections.emptySet())
                .message(NO_WINNING_TICKET)
                .build();
    }

    public WinningTicketResponseDto retrieveWinningNumbersByDate(LocalDateTime drawDate) {
        return winningNumbersRepository.findWinningNumbersByDrawDate(drawDate).stream()
                .map(winningNumbers -> WinningTicketResponseDto.builder()
                        .ticketId(winningNumbers.ticketId())
                        .drawDate(winningNumbers.drawDate())
                        .winningNumbers(winningNumbers.winningNumbers())
                        .build())
                .findAny()
                .orElseThrow(() -> new WinningNumbersNotFoundException(WINNING_NUMBERS_NOT_FOUND));
    }

    public boolean areWinningNumbersGeneratedByDate() {
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        return winningNumbersRepository.existsByDrawDate(nextDrawDate);
    }
}
