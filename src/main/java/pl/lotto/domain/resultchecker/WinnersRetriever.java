package pl.lotto.domain.resultchecker;

import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.resultchecker.dto.ResultResponseDto;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static pl.lotto.domain.resultchecker.ResultCheckerMapper.mapTicketResult;

public class WinnersRetriever {
    private static final int NUMBERS_WHEN_PLAYERS_WON = 3;

    public List<ResultResponseDto> retrieveWinners(List<TicketDto> tickets, Set<Integer> winningNumbers) {
        return tickets.stream().map(ticketDto -> {
                    Set<Integer> hitNumbers = calculateHits(winningNumbers, tickets);
                    return createResults(hitNumbers, ticketDto);
                })
                .toList();
    }

    private ResultResponseDto createResults(Set<Integer> hitNumbers, TicketDto ticket) {
        if(isWinner(hitNumbers)) {
            return mapTicketResult(hitNumbers, ticket);
        }
        return ResultResponseDto.builder()
                .isWinner(false)
                .build();
    }

    private boolean isWinner(Set<Integer> hitNumbers) {
        return hitNumbers.size() >= NUMBERS_WHEN_PLAYERS_WON;
    }

    private Set<Integer> calculateHits(Set<Integer> winningNumbers, List<TicketDto> tickets) {
        return tickets.stream().map(TicketDto::inputNumbers)
                .filter(inputNumbers -> inputNumbers.contains(winningNumber(winningNumbers)))
                .findAny()
                .orElse(Collections.emptySet());
    }

    private static Integer winningNumber(Set<Integer> winningNumbers) {
        return winningNumbers.stream()
                .findAny()
                .orElseThrow();
    }
}
