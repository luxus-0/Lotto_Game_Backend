package pl.lotto.domain.resultchecker;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WinnersRetriever {
    private static final int NUMBERS_WHEN_PLAYERS_WON = 3;

    public List<Player> retrieveWinners(List<Ticket> allTicketsByDate, Set<Integer> winningNumbers) {
        return allTicketsByDate.stream().map(ticket ->
                {
                    Set<Integer> hitNumbers = calculateHits(winningNumbers, ticket);
                    return createPlayer(ticket, hitNumbers);
                })
                .toList();
    }

    private Player createPlayer(Ticket ticket, Set<Integer> hitNumbers) {
        if (isWinner(hitNumbers)) {
            Player.builder()
                    .isWinner(true)
                    .build();
        }
            return Player.builder()
                    .hash(ticket.hash())
                    .numbers(ticket.numbers())
                    .hitNumbers(hitNumbers)
                    .drawDate(ticket.drawDate())
                    .isWinner(true)
                    .build();
    }

    private boolean isWinner(Set<Integer> hitNumbers) {
        return hitNumbers.size() >= NUMBERS_WHEN_PLAYERS_WON;
    }

    private Set<Integer> calculateHits(Set<Integer> winningNumbers, Ticket ticket) {
        return ticket.numbers().stream()
                .filter(winningNumbers::contains)
                .collect(Collectors.toSet());
    }
}
