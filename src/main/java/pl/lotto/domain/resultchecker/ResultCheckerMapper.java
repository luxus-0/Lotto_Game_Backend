package pl.lotto.domain.resultchecker;

import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.util.List;

class ResultCheckerMapper {

    static List<ResultDto> mapPlayersToResults(List<Player> players) {
        return players.stream()
                .map(player -> ResultDto.builder()
                        .hash(player.hash())
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
                        .hash(ticket.hash())
                        .numbers(ticket.numbers())
                        .drawDate(ticket.drawDate())
                        .build())
                .toList();
    }

}