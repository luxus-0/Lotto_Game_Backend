package pl.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.resultannouncer.ResultLotto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
class ResultCheckerMapper {

    static List<ResultLotto> mapPlayersToResults(List<Player> players) {
        return players.stream()
                .map(player -> ResultLotto.builder()
                        .ticketId("123456")
                        .numbers(Set.of(1,2,3,4,5,6))
                        .hitNumbers(Set.of(1,2))
                        .drawDate(player.drawDate())
                        .isWinner(true)
                        .build())
                .toList();
    }

    static List<Ticket> mapToTickets(List<TicketDto> allTicketsByDate) {
        return allTicketsByDate.stream()
                .map(ticket -> Ticket.builder()
                        .ticketId("123456")
                        .numbers(Set.of(1,2,3,4,5,6))
                        .drawDate(LocalDateTime.now(Clock.systemUTC()))
                        .build())
                .toList();
    }

    public static Player mapToPlayer(ResultLotto player) {
        return new Player(player.ticketId(), player.numbers(), player.hitNumbers(), player.drawDate(), player.isWinner(), player.message());
    }

}