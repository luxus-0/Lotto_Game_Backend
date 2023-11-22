package pl.lotto.domain.resultchecker;

import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numbersgenerator.WinningNumbersFacade;
import pl.lotto.domain.resultannouncer.ResultLotto;
import pl.lotto.domain.resultchecker.dto.PlayersDto;
import pl.lotto.domain.resultchecker.dto.ResultDto;
import pl.lotto.domain.resultchecker.exceptions.PlayerResultNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static pl.lotto.domain.resultchecker.ResultCheckerMapper.*;

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
            List<Player> players = winnersRetriever.retrieveWinners(mapToTickets(allTicketByDate), winningNumbers);
            playerRepository.saveAll(players);
            return PlayersDto.builder()
                    .results(mapToResults(players))
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
        Player player = mapToPlayer(players);
        return mapToResult(ticketId, player);
    }
}
