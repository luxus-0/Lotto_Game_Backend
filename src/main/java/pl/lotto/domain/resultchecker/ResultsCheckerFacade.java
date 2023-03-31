package pl.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numbersgenerator.WinningNumbersGeneratorFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;
import pl.lotto.domain.resultchecker.dto.PlayersDto;

import java.util.List;
import java.util.Set;

import static pl.lotto.domain.resultchecker.ResultCheckerMapper.*;

@AllArgsConstructor
public class ResultsCheckerFacade {

    NumberReceiverFacade numberReceiverFacade;

    DrawDateFacade drawDateFacade;

    WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;
    WinnersRetriever winnersRetriever;

    PlayerRepository playerRepository;

    public PlayersDto generateWinners() {
       List<TicketDto> allTicketByDate = numberReceiverFacade.retrieveAllTicketByNextDrawDate();
        List<Ticket> tickets = mapToTickets(allTicketByDate);
        WinningNumbersDto winningNumbersDto = winningNumbersGeneratorFacade.generateWinningNumbers();
       Set<Integer> winningNumbers = winningNumbersDto.winningNumbers();
       if(winningNumbers == null || winningNumbers.isEmpty()){
           return PlayersDto.builder()
                   .message("Winners not found")
                   .build();
       }

        List<Player> players = winnersRetriever.retrieveWinners(tickets, winningNumbers);
       playerRepository.saveAll(players);
       return PlayersDto.builder()
               .results(mapPlayersToResults(players))
               .message("Winners found")
               .build();
    }
}
