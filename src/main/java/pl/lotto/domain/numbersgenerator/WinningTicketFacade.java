package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketDto;
import pl.lotto.domain.numbersgenerator.exceptions.WinningNumbersNotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import static pl.lotto.domain.numbersgenerator.WinningNumbersManager.retrieveWinningNumbers;
import static pl.lotto.domain.numbersgenerator.WinningNumbersMessageProvider.WINNING_NUMBERS_NOT_FOUND;

@AllArgsConstructor
@Log4j2
@Builder
public class WinningTicketFacade {
    private final DrawDateFacade drawDateFacade;
    private final WinningNumbersRepository winningNumbersRepository;
    private final WinningNumberValidator winningNumberValidator;
    private final NumberReceiverFacade numberReceiverFacade;
    private final RandomNumbersGenerable randomNumbersGenerable;

    public WinningTicketDto generateWinningTicket() {
        String ticketId = randomNumbersGenerable.generateUniqueTicketId();
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        Set<Integer> randomNumbers = randomNumbersGenerable.generateSixRandomNumbers().randomNumbers();
        Set<Integer> userNumbers = numberReceiverFacade.retrieveUserNumbersByDrawDate(nextDrawDate);
        Set<Integer> winningNumbers = retrieveWinningNumbers(randomNumbers, userNumbers);
        boolean validate = winningNumberValidator.validate(winningNumbers);
        if (validate) {
            WinningTicket winningTicket = new WinningTicket(ticketId, winningNumbers, nextDrawDate);
            WinningTicket savedWinningTicket = winningNumbersRepository.save(winningTicket);
            log.info(savedWinningTicket);

            return WinningTicketDto.builder()
                    .ticketId(savedWinningTicket.ticketId())
                    .winningNumbers(savedWinningTicket.winningNumbers())
                    .drawDate(savedWinningTicket.drawDate())
                    .build();
        }
        return WinningTicketDto.builder()
                .winningNumbers(Collections.emptySet())
                .build();
    }

    public WinningTicketDto retrieveWinningNumbersByDate(LocalDateTime drawDate) {
        return winningNumbersRepository.findWinningNumbersByDrawDate(drawDate).stream()
                .map(winningNumbers -> WinningTicketDto.builder()
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
