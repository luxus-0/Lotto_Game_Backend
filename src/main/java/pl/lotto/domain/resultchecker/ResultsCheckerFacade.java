package pl.lotto.domain.resultchecker;

import lombok.extern.log4j.Log4j2;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numbersgenerator.WinningNumbersFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketResponseDto;
import pl.lotto.domain.resultchecker.dto.ResultResponseDto;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.resultchecker.exceptions.ResultNotFoundException;

import java.util.List;
import java.util.Set;

import static pl.lotto.domain.resultchecker.ResultCheckerMapper.mapToResultResponseDto;
import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.LOSE;
import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.TICKET_NOT_FOUND;

@Log4j2
public record ResultsCheckerFacade(NumberReceiverFacade numberReceiverFacade,
                                   DrawDateFacade drawDateFacade,
                                   WinningNumbersFacade winningNumbersFacade,
                                   WinnersRetriever winnersRetriever,
                                   ResultCheckerRepository resultCheckerRepository,
                                   ResultCheckerValidation resultCheckerValidation) {

    public ResultResponseDto generateResults() {
        WinningTicketResponseDto winningTicketResponse = winningNumbersFacade.generateWinningNumbers();
        Set<Integer> winningNumbers = winningTicketResponse.winningNumbers();
        boolean validate = resultCheckerValidation.validate(winningNumbers);
        if (validate) {
            List<TicketDto> tickets = numberReceiverFacade.retrieveTicketsByDrawDate(winningTicketResponse.drawDate());
            List<ResultResponseDto> winners = winnersRetriever.retrieveWinners(tickets, winningNumbers);
            log.info("Winners: " + winners);
            List<TicketResults> ticketsByUUID = resultCheckerRepository.findAllByTicketUUID(winningTicketResponse.ticketUUID()).stream().toList();
            List<TicketResults> ticketsSaved = resultCheckerRepository.saveAll(ticketsByUUID);
            log.info("Ticket saved to database: " +ticketsSaved);
            return mapToResultResponseDto(ticketsSaved);
        }
        return ResultResponseDto.builder()
                .isWinner(false)
                .message(LOSE)
                .build();
    }

    public ResultResponseDto findResultByTicketUUID(String ticketUUID) throws ResultNotFoundException {
        return resultCheckerRepository.findAllByTicketUUID(ticketUUID).stream()
                .map(ResultCheckerMapper::mapToResultResponse)
                .findAny()
                .orElseThrow(() -> new ResultNotFoundException(TICKET_NOT_FOUND));
    }
}
