package pl.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.resultannouncer.ResultLotto;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
class ResultCheckerMapper {

    static List<ResultLotto> mapPlayersToResults(List<Player> players) {
        return players.stream()
                .map(player -> ResultLotto.builder()
                        .ticketId(player.ticketId())
                        .numbers(player.numbers())
                        .hitNumbers(player.hitNumbers())
                        .drawDate(player.drawDate())
                        .isWinner(player.isWinner())
                        .message("aaa")
                        .build())
                .toList();
    }

    static List<Ticket> mapToTickets(List<TicketDto> allTicketsByDate) {
        if(allTicketsByDate.size() == 0){
            throw new IllegalArgumentException("Not tickets available");
        }
        return allTicketsByDate.stream()
                .map(ticket -> Ticket.builder()
                        .ticketId(ticket.ticketId())
                        .numbers(Collections.emptySet())
                        .drawDate(ticket.drawDate())
                        .build())
                .toList();
    }

    public static Player mapToPlayer(ResultLotto player) {
        return new Player(player.ticketId(), player.numbers(), player.hitNumbers(), player.drawDate(), player.isWinner(), player.message());
    }

}