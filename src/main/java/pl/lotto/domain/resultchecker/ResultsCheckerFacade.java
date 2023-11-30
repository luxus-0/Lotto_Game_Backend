package pl.lotto.domain.resultchecker;

import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numbersgenerator.WinningTicketFacade;
import pl.lotto.domain.resultannouncer.ResultLotto;
import pl.lotto.domain.resultchecker.dto.PlayersDto;
import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static pl.lotto.domain.resultchecker.ResultCheckerMapper.*;
import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.PLAYER_LOSE;

public record ResultsCheckerFacade(NumberReceiverFacade numberReceiverFacade,
                                   DrawDateFacade drawDateFacade,
                                   WinningTicketFacade winningTicketFacade,
                                   WinnersRetriever winnersRetriever,
                                   PlayerRepository playerRepository,
                                   ResultCheckerValidation resultCheckerValidation) {

    public PlayersDto generateResults() {
        Set<Integer> winningNumbers = winningTicketFacade.generateWinningNumbers().winningNumbers();
        boolean validate = resultCheckerValidation.validate(winningNumbers);
        if (validate) {
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        List<TicketDto> tickets = numberReceiverFacade.retrieveAllTicketByDrawDate(nextDrawDate);
            List<Player> players = winnersRetriever.retrieveWinners(mapToTickets(tickets), winningNumbers);
            playerRepository.saveAll(players);
            return PlayersDto.builder()
                    .results(mapToPlayerResults(players))
                    .build();
        }
       return PlayersDto.builder()
              .results(List.of(ResultLotto.builder()
                      .message(PLAYER_LOSE)
                     .build()))
               .build();
    }

    public ResultDto findResultByTicketId(String ticketId) {
        PlayersDto players = generateResults();
        Player player = mapToPlayer(players);
        return mapToPlayerResult(ticketId, player);
    }
}
