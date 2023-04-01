package pl.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numbersgenerator.WinningNumbersGeneratorFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;
import pl.lotto.domain.resultchecker.dto.PlayersDto;
import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.util.List;
import java.util.Set;

import static pl.lotto.domain.resultchecker.ResultCheckerMapper.mapPlayersToResults;
import static pl.lotto.domain.resultchecker.ResultCheckerMapper.mapToTickets;

@AllArgsConstructor
public class ResultsCheckerFacade {

    NumberReceiverFacade numberReceiverFacade;

    DrawDateFacade drawDateFacade;

    WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;
    WinnersRetriever winnersRetriever;

    PlayerRepository playerRepository;

    ResultValidation resultValidation;

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

       resultValidation.validate(winningNumbers);
       List<Player> players = winnersRetriever.retrieveWinners(tickets, winningNumbers);
       playerRepository.saveAll(players);
       return PlayersDto.builder()
               .results(mapPlayersToResults(players))
               .tickets(tickets)
               .message("Winners found")
               .build();
    }

    ResultDto findByHash(String hash){
        Player player = playerRepository.findById(hash).orElseThrow(() -> new PlayerResultNotFoundException("Player not win"));
        return ResultDto.builder()
                .hash(hash)
                .numbers(player.numbers())
                .hitNumbers(player.hitNumbers())
                .drawDate(player.drawDate())
                .isWinner(player.isWinner())
                .build();
    }
}
