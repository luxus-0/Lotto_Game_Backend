package pl.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numberreceiver.exceptions.WinningTicketNotFoundException;
import pl.lotto.domain.numbersgenerator.WinningNumbersFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketResponseDto;
import pl.lotto.domain.resultchecker.dto.ResultResponseDto;
import pl.lotto.domain.resultchecker.exceptions.ResultCheckerNotFoundException;

import java.util.List;
import java.util.Set;

import static pl.lotto.domain.resultchecker.ResultCheckerMapper.mapToResultResponseDto;
import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.LOSE;

@Log4j2
@AllArgsConstructor
public class ResultsCheckerFacade {
    private final NumberReceiverFacade numberReceiverFacade;
    private final DrawDateFacade drawDateFacade;
    private final WinningNumbersFacade winningNumbersFacade;
    private final WinnersRetriever winnersRetriever;
    private final ResultCheckerRepository resultCheckerRepository;
    private final ResultCheckerValidation resultCheckerValidation;

    @Cacheable("results")
    public ResultResponseDto generateResults() {
        WinningTicketResponseDto winningTicketResponse = winningNumbersFacade.generateWinningNumbers();
        Set<Integer> winningNumbers = winningTicketResponse.winningNumbers();
        boolean validate = resultCheckerValidation.validate(winningNumbers);
        if (validate) {
            List<TicketDto> tickets = numberReceiverFacade.retrieveTicketsByDrawDate(winningTicketResponse.drawDate());
            List<ResultResponseDto> winTicket = generateWinningTicket(tickets, winningNumbers);
            log.info("Winning Ticket: " + winTicket);
            TicketResults ticket = resultCheckerRepository.findAllByTicketUUID(winningTicketResponse.ticketUUID())
                    .orElseThrow(() -> new WinningTicketNotFoundException("Winning ticket not found"));
            List<TicketResults> ticketsSaved = resultCheckerRepository.saveAll(List.of(ticket));
            log.info("Ticket saved: " +ticketsSaved);
            return mapToResultResponseDto(ticketsSaved);
        }
        return ResultResponseDto.builder()
                .isWinner(false)
                .message(LOSE)
                .build();
    }

    public ResultResponseDto findResultByTicketUUID(String ticketUUID) {
        return resultCheckerRepository.findAllByTicketUUID(ticketUUID).stream()
                .map(ResultCheckerMapper::mapToResultResponse)
                .findAny()
                .orElseThrow(() -> new ResultCheckerNotFoundException("Not found for ticket uuid: " + ticketUUID));
    }

    public List<ResultResponseDto> generateWinningTicket(List<TicketDto> tickets, Set<Integer> winningNumbers){
        return winnersRetriever.retrieveWinners(tickets, winningNumbers);
    }
}
