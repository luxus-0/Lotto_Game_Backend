package pl.lotto.domain.resultchecker;

import lombok.extern.log4j.Log4j2;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.exceptions.WinningTicketNotFoundException;
import pl.lotto.domain.numbersgenerator.WinningTicketFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketResponseDto;
import pl.lotto.domain.resultchecker.dto.ResultResponseDto;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.resultchecker.exceptions.ResultNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static pl.lotto.domain.resultchecker.ResultCheckerMapper.mapToResultResponseDto;
import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.LOSE;
import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.TICKET_NOT_FOUND;

@Log4j2
public record ResultsCheckerFacade(NumberReceiverFacade numberReceiverFacade,
                                   DrawDateFacade drawDateFacade,
                                   WinningTicketFacade winningTicketFacade,
                                   WinnersRetriever winnersRetriever,
                                   ResultCheckerRepository resultCheckerRepository,
                                   ResultCheckerValidation resultCheckerValidation) {

    public ResultResponseDto generateResults() throws WinningTicketNotFoundException {
        WinningTicketResponseDto winningTicketResponse = winningTicketFacade.generateWinningTicket();
        Set<Integer> winningNumbers = winningTicketResponse.winningNumbers();
        boolean validate = resultCheckerValidation.validate(winningNumbers);
        if (validate) {
            List<TicketDto> ticketDtos = numberReceiverFacade.retrieveTicketsByDrawDate(winningTicketResponse.drawDate());
            List<ResultResponseDto> winners = winnersRetriever.retrieveWinners(ticketDtos, winningNumbers);
            log.info("Winners: " + winners);
            List<TicketResults> tickets = resultCheckerRepository.findAllByTicketUUID(winningTicketResponse.ticketUUID()).stream().toList();
            List<TicketResults> ticketsSaved = resultCheckerRepository.saveAll(tickets);
            log.info(ticketsSaved);
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
