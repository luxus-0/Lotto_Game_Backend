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

    public PlayersDto generateResults() {
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

        resultValidation.validate(winningNumbers);
        List<Player> players = winnersRetriever.retrieveWinners(tickets, winningNumbers);
        playerRepository.saveAll(players);
        return PlayersDto.builder()
                .results(mapPlayersToResults(players))
                .message("Winners found")
                .build();
    }

    public ResultDto findResultByTicketId(String ticketId) {
        Player player = playerRepository.findByTicketId(ticketId).orElseThrow(() -> new PlayerResultNotFoundException("Not found for id: " + ticketId));
        if (player != null) {
            return ResultDto.builder()
                    .hash(player.ticketId())
                    .numbers(player.numbers())
                    .hitNumbers(player.hitNumbers())
                    .drawDate(player.drawDate())
                    .build();
        }
        return ResultDto.builder()
                .isWinner(false)
                .build();
    }

}
