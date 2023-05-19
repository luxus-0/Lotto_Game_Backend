package pl.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numbersgenerator.WinningNumbersFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;
import pl.lotto.domain.resultannouncer.ResultLotto;
import pl.lotto.domain.resultchecker.dto.PlayersDto;
import pl.lotto.domain.resultchecker.dto.ResultDto;
import pl.lotto.domain.resultchecker.exceptions.PlayerResultNotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static pl.lotto.domain.resultchecker.ResultCheckerMapper.mapPlayersToResults;
import static pl.lotto.domain.resultchecker.ResultCheckerMapper.mapToTickets;

@AllArgsConstructor
public class ResultsCheckerFacade {

    NumberReceiverFacade numberReceiverFacade;
    DrawDateFacade drawDateFacade;
    WinningNumbersFacade winningNumbersFacade;
    WinnersRetriever winnersRetriever;
    PlayerRepository playerRepository;
    ResultValidation resultValidation;

    public PlayersDto generateResults() {
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        List<TicketDto> allTicketByDate = numberReceiverFacade.retrieveAllTicketByDrawDate(nextDrawDate);
        WinningNumbersDto winningNumbersDto = winningNumbersFacade.generateWinningNumbers();
        Set<Integer> winningNumbers = winningNumbersDto.winningNumbers();

        if (winningNumbers == null || winningNumbers.isEmpty()) {
            return PlayersDto.builder()
                    .results(Collections.emptyList())
                    .build();
        }

        resultValidation.validate(winningNumbers);

        List<Ticket> tickets = mapToTickets(allTicketByDate);
        List<Player> players = winnersRetriever.retrieveWinners(tickets, winningNumbers);
        List<ResultLotto> results = mapPlayersToResults(players);
        playerRepository.saveAll(players);
        return PlayersDto.builder()
                .results(results)
                .build();
    }

    public ResultDto findResultByTicketId(String ticketId) {
        PlayersDto players = generateResults();
        List<ResultLotto> results =  players.results();
        Player player = results.stream()
                .map(ResultCheckerMapper::mapToPlayer)
                .findAny()
                .orElseThrow(() -> new PlayerResultNotFoundException("Player result not found"));
        Player searchPlayer = playerRepository.findPlayerByTicketId(player.ticketId()).orElseThrow(() -> new PlayerResultNotFoundException("Not found for id: " +ticketId));
            return ResultDto.builder()
                    .ticketId(searchPlayer.ticketId())
                    .numbers(searchPlayer.numbers())
                    .hitNumbers(searchPlayer.hitNumbers())
                    .drawDate(searchPlayer.drawDate())
                    .isWinner(searchPlayer.isWinner())
                    .build();
        }



}
