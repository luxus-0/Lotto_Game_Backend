package pl.lotto.domain.resultchecker;

import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.resultannouncer.ResultLotto;

import java.util.List;

class ResultCheckerMapper {

    static List<ResultLotto> mapPlayersToResults(List<Player> players) {
        return players.stream()
                .map(player -> ResultLotto.builder()
                        .ticketId(player.ticketId())
                        .numbers(player.numbers())
                        .hitNumbers(player.hitNumbers())
                        .drawDate(player.drawDate())
                        .isWinner(true)
                        .build())
                .toList();
    }

    static List<Ticket> mapToTickets(List<TicketDto> allTicketsByDate) {
        return allTicketsByDate.stream()
                .map(ticket -> Ticket.builder()
                        .ticketId(ticket.ticketId())
                        .numbers(ticket.numbers())
                        .drawDate(ticket.drawDate())
                        .build())
                .toList();
    }

}