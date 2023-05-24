package pl.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import pl.lotto.domain.drawdate.AdjustableClock;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.TicketIdGenerator;
import pl.lotto.domain.numberreceiver.TicketIdGeneratorImpl;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numbersgenerator.InMemoryRandomNumberGenerator;
import pl.lotto.domain.numbersgenerator.RandomNumbersGenerable;
import pl.lotto.domain.numbersgenerator.WinningNumbers;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;
import pl.lotto.domain.resultannouncer.ResultLotto;

import java.time.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static pl.lotto.domain.resultchecker.ResultCheckerGeneratorNumbers.generateNumbers;

@AllArgsConstructor
class ResultCheckerMapper {

    private final static RandomNumbersGenerable randomNumbers = new InMemoryRandomNumberGenerator();
    private final static AdjustableClock clock = new AdjustableClock(Instant.now(), ZoneId.systemDefault());

    static List<ResultLotto> mapPlayersToResults(List<Player> players) {
        WinningNumbersDto numbers = randomNumbers.generateWinnerNumbers(Set.of(1,2));
        return players.stream()
                .map(player -> ResultLotto.builder()
                        .ticketId(randomNumbers.generateUniqueTicketId())
                        .numbers(randomNumbers.generateSixRandomNumbers().randomNumbers())
                        .hitNumbers(numbers.winningNumbers())
                        .drawDate(LocalDateTime.now(clock))
                        .isWinner(true)
                        .build())
                .toList();
    }

    static List<Ticket> mapToTickets(List<TicketDto> allTicketsByDate) {
        return allTicketsByDate.stream()
                .map(ticket -> Ticket.builder()
                        .ticketId(randomNumbers.generateUniqueTicketId())
                        .numbers(randomNumbers.generateSixRandomNumbers().randomNumbers())
                        .drawDate(LocalDateTime.now(clock))
                        .build())
                .toList();
    }

    public static Player mapToPlayer(ResultLotto player) {
        return new Player(player.ticketId(), player.numbers(), player.hitNumbers(), player.drawDate(), player.isWinner(), player.message());
    }

}