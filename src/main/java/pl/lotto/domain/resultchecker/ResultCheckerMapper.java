package pl.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.resultannouncer.ResultLotto;
import pl.lotto.domain.resultchecker.dto.PlayerResultsDto;
import pl.lotto.domain.resultchecker.dto.ResultDto;
import pl.lotto.domain.resultchecker.exceptions.PlayerResultNotFoundException;

import java.util.List;

import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.PLAYER_NOT_FOUND;

@AllArgsConstructor
class ResultCheckerMapper {

    static List<ResultLotto> mapToPlayerResults(List<Player> players) {
        return players.stream()
                .map(player -> ResultLotto.builder()
                        .ticketUUID(player.ticketUUID())
                        .numbers(player.numbers())
                        .hitNumbers(player.hitNumbers())
                        .drawDate(player.drawDate())
                        .isWinner(player.isWinner())
                        .message(player.message())
                        .build())
                .toList();
    }

    static Player mapToPlayer(PlayerResultsDto players) {
        return players.results()
                .stream()
                .map(ResultCheckerMapper::mapToPlayer)
                .findAny()
                .orElseThrow(() -> new PlayerResultNotFoundException(PLAYER_NOT_FOUND));
    }

    static Player mapToPlayer(ResultLotto player) {
        return new Player(
                player.ticketUUID(),
                player.numbers(),
                player.hitNumbers(),
                player.drawDate(),
                player.isWinner(),
                player.message());
    }

    static ResultDto mapToPlayerResult(String ticketUUID, Player player) {
        return ResultDto.builder()
                .ticketUUID(ticketUUID)
                .numbers(player.numbers())
                .hitNumbers(player.hitNumbers())
                .drawDate(player.drawDate())
                .isWinner(player.isWinner())
                .message(player.message())
                .build();
    }

    public static List<Ticket> mapToTickets(List<TicketDto> tickets) {
        return tickets.stream()
                .map(ticket -> Ticket.builder()
                        .ticketUUID(ticket.ticketUUID())
                        .numbers(ticket.numbers())
                        .hitNumbers(ticket.hitNumbers())
                        .drawDate(ticket.drawDate())
                        .isWinner(ticket.isWinner())
                        .message(ticket.ticketResultMessage().getMessage())
                        .build())
                .toList();
    }

}