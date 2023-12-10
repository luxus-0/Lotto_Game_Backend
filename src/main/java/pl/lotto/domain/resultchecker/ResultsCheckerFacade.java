package pl.lotto.domain.resultchecker;

import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numbersgenerator.WinningNumbersFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketResponseDto;
import pl.lotto.domain.resultchecker.dto.PlayerResultsDto;
import pl.lotto.domain.resultchecker.dto.ResultDto;
import pl.lotto.domain.resultchecker.exceptions.PlayerResultNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static pl.lotto.domain.resultchecker.ResultCheckerMapper.*;
import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.PLAYER_LOSE;

public record ResultsCheckerFacade(NumberReceiverFacade numberReceiverFacade,
                                   DrawDateFacade drawDateFacade,
                                   WinningNumbersFacade winningNumbersFacade,
                                   WinnersRetriever winnersRetriever,
                                   PlayerRepository playerRepository,
                                   ResultCheckerValidation resultCheckerValidation) {

    public PlayerResultsDto generateResults() {
        WinningTicketResponseDto winningTicketResponse = winningNumbersFacade.generateWinningNumbers();
        Set<Integer> winningNumbers = winningTicketResponse.winningNumbers();
        boolean validate = resultCheckerValidation.validate(winningNumbers);
        if (validate) {
            LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
            List<TicketDto> tickets = numberReceiverFacade.retrieveAllTicketByDrawDate(nextDrawDate);
            List<Player> players = winnersRetriever.retrieveWinners(mapToTickets(tickets), winningNumbers);
            List<Player> playerSaved = playerRepository.saveAll(players);
            return PlayerResultsDto.builder()
                    .results(mapToPlayerResults(playerSaved))
                    .build();
        }
        throw new PlayerResultNotFoundException(PLAYER_LOSE);
    }

    public ResultDto findResultByTicketUUID(String ticketUUID) {
        PlayerResultsDto players = generateResults();
        Player player = mapToPlayer(players);
        ResultDto resultDto = mapToPlayerResult(ticketUUID, player);
        if(resultDto == null){
            throw new PlayerResultNotFoundException(PLAYER_LOSE);
        }
        return resultDto;
    }
}
