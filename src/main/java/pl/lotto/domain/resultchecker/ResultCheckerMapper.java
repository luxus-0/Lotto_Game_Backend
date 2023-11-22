package pl.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import pl.lotto.domain.drawdate.AdjustableClock;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numbersgenerator.InMemoryRandomNumberGenerator;
import pl.lotto.domain.numbersgenerator.RandomNumbersGenerable;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;
import pl.lotto.domain.numbersgenerator.exceptions.WinnerNumbersNotFoundException;
import pl.lotto.domain.resultannouncer.ResultLotto;
import pl.lotto.domain.resultchecker.dto.PlayersDto;
import pl.lotto.domain.resultchecker.dto.ResultDto;
import pl.lotto.domain.resultchecker.exceptions.PlayerResultNotFoundException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
class ResultCheckerMapper {

    static List<ResultLotto> mapToResults(List<Player> players) {
        return players.stream()
                .map(player -> ResultLotto.builder()
                        .ticketId(player.ticketId())
                        .numbers(player.numbers())
                        .hitNumbers(player.hitNumbers())
                        .drawDate(player.drawDate())
                        .isWinner(player.isWinner())
                        .message(player.message())
                        .build())
                .toList();
    }

    static List<Ticket> mapToTickets(List<TicketDto> allTicketsByDate) {
        return allTicketsByDate.stream()
                .map(ticket -> Ticket.builder()
                        .ticketId(ticket.ticketId())
                        .numbers(ticket.numbers())
                        .drawDate(ticket.drawDate())
                        .message(ticket.message())
                        .build())
                .toList();
    }

    static Player mapToPlayer(PlayersDto players) {
        return players.results()
                .stream()
                .map(ResultCheckerMapper::mapToPlayer)
                .findAny()
                .orElseThrow(() -> new PlayerResultNotFoundException("Player result not found"));
    }

    static Player mapToPlayer(ResultLotto player) {
        return new Player(player.ticketId(), player.numbers(), player.hitNumbers(), player.drawDate(), player.isWinner(), player.message());
    }

    static ResultDto mapToResult(String ticketId, Player player) {
        return ResultDto.builder()
                .ticketId(ticketId)
                .numbers(player.numbers())
                .hitNumbers(player.hitNumbers())
                .drawDate(player.drawDate())
                .isWinner(player.isWinner())
                .message(player.message())
                .build();
    }

}