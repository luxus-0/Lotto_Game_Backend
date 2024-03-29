package pl.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.numberreceiver.exceptions.WinningTicketNotFoundException;
import pl.lotto.domain.resultchecker.dto.TicketResponseDto;
import pl.lotto.domain.resultchecker.exceptions.ResultCheckerNotFoundException;
import pl.lotto.domain.resultchecker.exceptions.TicketNotSavedException;
import pl.lotto.domain.winningnumbers.WinningNumbersFacade;
import pl.lotto.domain.winningnumbers.WinningTicket;
import pl.lotto.domain.winningnumbers.dto.WinningTicketResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static pl.lotto.domain.resultchecker.ResultCheckerMapper.mapToResultResponseDto;
import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.LOSE;

@Log4j2
@AllArgsConstructor
public class ResultsCheckerFacade {
    private final NumberReceiverFacade numberReceiverFacade;
    private final WinningNumbersFacade winningNumbersFacade;
    private final WinningTicketManager winningTicketManager;
    private final ResultCheckerRepository resultCheckerRepository;
    private final ResultCheckerValidation resultCheckerValidation;

    @Cacheable("results")
    public TicketResponseDto generateResults() throws Exception {
        WinningTicketResponseDto winningTicketResponse = winningNumbersFacade.generateWinningNumbers();
        boolean validate = resultCheckerValidation.validate(winningTicketResponse.winningNumbers());
        String ticketUUID = winningTicketResponse.ticketUUID();
        LocalDateTime drawDate = winningTicketResponse.drawDate();
        Set<Integer> winningNumbers = winningTicketResponse.winningNumbers();
        if (validate) {
            List<TicketDto> tickets = numberReceiverFacade.retrieveTicketsByDrawDate(drawDate);
            WinningTicket winningTicket = resultCheckerRepository.findAllByTicketUUID(ticketUUID)
                    .orElseThrow(WinningTicketNotFoundException::new);
            List<TicketResponseDto> winningTickets = generateWinTicket(winningNumbers, tickets);
            log.info("Winning ticket: " +winningTickets);
            List<WinningTicket> ticketsSaved = resultCheckerRepository.saveAll(List.of(winningTicket));
            if(ticketsSaved.isEmpty()){
                throw new TicketNotSavedException();
            }
            log.info("Winning ticket saved : " +ticketsSaved);
            return mapToResultResponseDto(ticketsSaved);
        }
        return TicketResponseDto.builder()
                .isWinner(false)
                .message(LOSE)
                .build();
    }

    public TicketResponseDto findResultByTicketUUID(String ticketUUID) throws Exception {
        return resultCheckerRepository.findAllByTicketUUID(ticketUUID).stream()
                .map(ResultCheckerMapper::mapToResultResponse)
                .findAny()
                .orElseThrow(() -> new ResultCheckerNotFoundException(ticketUUID));
    }

    public List<TicketResponseDto> generateWinTicket(Set<Integer> winningNumbers, List<TicketDto> tickets){
        return winningTicketManager.retrieveWinners(winningNumbers, tickets);
    }
}
