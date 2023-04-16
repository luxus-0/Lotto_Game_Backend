package pl.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numbersgenerator.WinningNumbersFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;
import pl.lotto.domain.resultchecker.dto.PlayersDto;
import pl.lotto.domain.resultchecker.dto.ResultDto;
import pl.lotto.domain.resultchecker.exceptions.PlayerResultNotFoundException;

import java.time.LocalDateTime;
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

    public PlayersDto generateWinners() {
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        List<TicketDto> allTicketByDate = numberReceiverFacade.retrieveAllTicketByDrawDate(nextDrawDate);
        List<Ticket> tickets = mapToTickets(allTicketByDate);
        WinningNumbersDto winningNumbersDto = winningNumbersFacade.generateWinningNumbers();
        Set<Integer> winningNumbers = winningNumbersDto.winningNumbers();
        if (winningNumbers == null || winningNumbers.isEmpty()) {
            return PlayersDto.builder()
                    .message("Winners not found")
                    .build();
        }

        ResultDto resultDto = resultValidation.validate(winningNumbers);
        List<Player> players = winnersRetriever.retrieveWinners(tickets, resultDto.numbers());
        playerRepository.saveAll(players);
        return PlayersDto.builder()
                .results(mapPlayersToResults(players))
                .tickets(tickets)
                .message("Winners found")
                .build();
    }

    public ResultDto findByHash(String hash) {
        Player playerByHash = playerRepository.findById(hash).orElseThrow(() -> new PlayerResultNotFoundException("Not found for id: " + hash));
        if (playerByHash != null) {
            return ResultDto.builder()
                    .hash(playerByHash.hash())
                    .numbers(playerByHash.numbers())
                    .hitNumbers(playerByHash.hitNumbers())
                    .drawDate(playerByHash.drawDate())
                    .isWinner(true)
                    .build();
        }
        return ResultDto.builder()
                .isWinner(false)
                .build();
    }

}
