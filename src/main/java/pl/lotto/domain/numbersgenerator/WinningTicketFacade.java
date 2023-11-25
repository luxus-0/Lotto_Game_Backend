package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketDto;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketMessageDto;
import pl.lotto.domain.numbersgenerator.exceptions.WinnerNumbersNotFoundException;
import pl.lotto.domain.numbersgenerator.exceptions.WinningNumbersNotFoundException;
import pl.lotto.infrastructure.numbergenerator.client.RandomNumberClient;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import static pl.lotto.domain.numbersgenerator.WinningNumbersMessageProvider.WINNING_NUMBERS_NOT_FOUND;

@AllArgsConstructor
@Log4j2
@Builder
public class WinningTicketFacade {
    private final DrawDateFacade drawDateFacade;
    private final WinningNumbersRepository winningNumbersRepository;
    private final WinningNumberValidator winningNumberValidator;
    private final WinningTicketManager winningTicket;
    private final RandomNumbersGenerable randomNumbersGenerable;

    public WinningTicketDto generateWinningTicket() throws WinnerNumbersNotFoundException {
        String ticketId = randomNumbersGenerable.generateUniqueTicketId();
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        RandomNumbersDto sixRandomNumbers = randomNumbersGenerable.generateSixRandomNumbers();
        log.info("Six random numbers: " + sixRandomNumbers);
        Set<Integer> winningNumbers = winningTicket.getWinningNumbers(nextDrawDate);
        boolean validate = winningNumberValidator.validate(winningNumbers);
        if (validate) {
            WinningTicket winnerTicket = winningTicket.getWinnerTicket(ticketId, winningNumbers, nextDrawDate);
            WinningTicket savedWinnerTicket = winningNumbersRepository.save(winnerTicket);
            log.info(savedWinnerTicket);
            return winningTicket.getSavedWinnerTicket(savedWinnerTicket);
        }
        return WinningTicketDto.builder()
                .winningNumbers(Collections.emptySet())
                .winningTicketMessage(new WinningTicketMessageDto("Ticket not win"))
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
