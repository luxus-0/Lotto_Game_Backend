package pl.lotto.domain.resultchecker;

import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numberreceiver.exceptions.WinningTicketNotFoundException;
import pl.lotto.domain.numbersgenerator.WinningTicketFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketResponseDto;
import pl.lotto.domain.resultchecker.dto.ResultCheckerResponseDto;
import pl.lotto.domain.resultchecker.exceptions.ResultNotFoundException;

import java.util.List;
import java.util.Set;

import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.TICKET_NOT_FOUND;

public record ResultsCheckerFacade(NumberReceiverFacade numberReceiverFacade,
                                   DrawDateFacade drawDateFacade,
                                   WinningTicketFacade winningTicketFacade,
                                   WinnersRetriever winnersRetriever,
                                   ResultCheckerRepository resultCheckerRepository,
                                   ResultCheckerValidation resultCheckerValidation) {

    public ResultCheckerResponseDto generateResults() throws ResultNotFoundException, WinningTicketNotFoundException {
        WinningTicketResponseDto winningTicketResponse = winningTicketFacade.generateWinningTicket();
        Set<Integer> winningNumbers = winningTicketResponse.winningNumbers();
        boolean validate = resultCheckerValidation.validate(winningNumbers);
        if (validate) {
            List<TicketDto> tickets = numberReceiverFacade.retrieveTicketsByDrawDate(winningTicketResponse.drawDate());
            Set<ResultCheckerResponse> ticketsWinningNumbers = winnersRetriever.retrieveWinners(tickets, winningNumbers);
            List<ResultCheckerResponse> ticketWinningNumbersSaved = resultCheckerRepository.saveAll(ticketsWinningNumbers);
            return ResultCheckerResponseDto.builder()
                    .ticketsWinningNumbersSaved(ticketWinningNumbersSaved)
                    .build();
        }
        throw new ResultNotFoundException(TICKET_NOT_FOUND);
    }

    public ResultCheckerResponseDto findResultByTicketUUID(String ticketUUID) throws Exception {
        return resultCheckerRepository.findAllByTicketUUID(ticketUUID)
                .map(ResultCheckerMapper::mapToTicketWinningNumbers)
                .orElseThrow(() -> new ResultNotFoundException(TICKET_NOT_FOUND));
    }
}
