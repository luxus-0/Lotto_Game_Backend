package pl.lotto.domain.resultchecker;

import pl.lotto.domain.numberreceiver.dto.TicketDto;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.lotto.domain.resultchecker.ResultCheckerMapper.mapPlayerLose;
import static pl.lotto.domain.resultchecker.ResultCheckerMapper.mapPlayerWin;

public class WinnersRetriever {
    private static final int NUMBERS_WHEN_PLAYERS_WON = 3;

    public Set<ResultCheckerResponse> retrieveWinners(List<TicketDto> tickets, Set<Integer> winningNumbers) {
        return tickets.stream().map(ticketDto -> {
                    Set<Integer> hitNumbers = calculateHits(winningNumbers, tickets);
                    return createResults(hitNumbers, ticketDto);
                })
                .collect(Collectors.toSet());
    }

    private ResultCheckerResponse createResults(Set<Integer> hitNumbers, TicketDto ticket) {
        if(isWinner(hitNumbers)) {
            return mapPlayerWin(hitNumbers, ticket);
        }
        return mapPlayerLose(hitNumbers, ticket);
    }

    private boolean isWinner(Set<Integer> hitNumbers) {
        return hitNumbers.size() > NUMBERS_WHEN_PLAYERS_WON;
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
