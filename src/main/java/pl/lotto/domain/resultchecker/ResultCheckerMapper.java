package pl.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import pl.lotto.domain.drawdate.AdjustableClock;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numbersgenerator.InMemoryRandomNumberGenerator;
import pl.lotto.domain.numbersgenerator.RandomNumbersGenerable;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;
import pl.lotto.domain.numbersgenerator.exceptions.WinnerNumbersNotFoundException;
import pl.lotto.domain.resultannouncer.ResultLotto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
                        .message("WIN")
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

    public static Player mapToPlayer(ResultLotto player) {
        return new Player(player.ticketId(), player.numbers(), player.hitNumbers(), player.drawDate(), player.isWinner(), player.message());
    }

}