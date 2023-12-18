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

import static pl.lotto.domain.numbersgenerator.RandomNumbersURL.*;
import static pl.lotto.domain.numbersgenerator.WinningNumbersValidationResult.WINNING_NUMBERS_NOT_FOUND;
import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.LOSE;

@AllArgsConstructor
@Log4j2
@Builder
public class WinningTicketFacade {
    private final DrawDateFacade drawDateFacade;
    private final WinningNumbersRepository winningNumbersRepository;
    private final WinningNumbersValidator winningNumbersValidator;
    private final NumberReceiverFacade numberReceiverFacade;
    private final RandomNumbersGenerator randomNumbersGenerator;
    private final WinningNumbersConfigurationProperties properties;
    private final WinningNumbersManager winningNumbersManager;

    public WinningTicketResponseDto generateWinningTicket() {
        String ticketId = randomNumbersGenerator.generateTicketUUID();
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        RandomNumbersResponseDto randomNumbersResponse = randomNumbersGenerator.generateRandomNumbers(COUNT_RANDOM_NUMBERS, LOWER_BAND_RANDOM_NUMBERS, UPPER_BAND_RANDOM_NUMBERS);
        Set<Integer> randomNumbers = randomNumbersResponse.randomNumbers();
        Set<Integer> inputNumbers = numberReceiverFacade.retrieveInputNumbersByDrawDate(nextDrawDate);
        Set<Integer> winningNumbers = winningNumbersManager.retrieveWinningNumbers(randomNumbers, inputNumbers);
        boolean validate = winningNumbersValidator.validate(winningNumbers);
        if (validate) {
            WinningNumbers winningTicket = new WinningNumbers(ticketId, winningNumbers, nextDrawDate);
            WinningNumbers savedWinningNumbers = winningNumbersRepository.save(winningTicket);

            return WinningTicketResponseDto.builder()
                    .ticketUUID(savedWinningNumbers.ticketUUID())
                    .winningNumbers(savedWinningNumbers.winningNumbers())
                    .drawDate(savedWinningNumbers.drawDate())
                    .build();
        }
        return WinningTicketResponseDto.builder()
                .winningNumbers(Set.of())
                .message(LOSE)
                .build();
    }

    public WinningTicketResponseDto retrieveWinningNumbersByDate(LocalDateTime drawDate) {
        return winningNumbersRepository.findWinningNumbersByDrawDate(drawDate).stream()
                .map(winningNumbers -> WinningTicketResponseDto.builder()
                        .ticketUUID(winningNumbers.ticketUUID())
                        .drawDate(winningNumbers.drawDate())
                        .winningNumbers(winningNumbers.winningNumbers())
                        .build())
                .findAny()
                .orElseThrow(() -> new WinningNumbersNotFoundException(WINNING_NUMBERS_NOT_FOUND.getMessage()));
    }

    public boolean areWinningNumbersGeneratedByDate() {
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        return winningNumbersRepository.existsByDrawDate(nextDrawDate);
    }
}
