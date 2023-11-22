package pl.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import lombok.Getter;
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

public record ResultsCheckerFacade(NumberReceiverFacade numberReceiverFacade,
                                   DrawDateFacade drawDateFacade,
                                   WinningNumbersFacade winningNumbersFacade,
                                   WinnersRetriever winnersRetriever,
                                   PlayerRepository playerRepository,
                                   ResultCheckerValidation resultCheckerValidation) {
    public PlayersDto generateResults() {
        Set<Integer> winningNumbers = winningNumbersFacade.generateWinningNumbers().winningNumbers();
        boolean validate = resultCheckerValidation.validate(winningNumbers);
        if (validate) {
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        List<TicketDto> allTicketByDate = numberReceiverFacade.retrieveAllTicketByDrawDate(nextDrawDate);
            List<Ticket> tickets = mapToTickets(allTicketByDate);
            List<Player> players = winnersRetriever.retrieveWinners(tickets, winningNumbers);
            List<ResultLotto> results = mapPlayersToResults(players);
            playerRepository.saveAll(players);
            return PlayersDto.builder()
                    .results(results)
                    .build();
        }
        return PlayersDto.builder()
                .results(List.of(ResultLotto.builder()
                        .message("LOSE")
                        .build()))
                .build();

    }

    public ResultDto findResultByTicketId(String ticketId) {
        PlayersDto players = generateResults();
        Player player = players.results()
                .stream()
                .map(ResultCheckerMapper::mapToPlayer)
                .findAny()
                .orElseThrow(() -> new PlayerResultNotFoundException("Player result not found"));
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
