package pl.lotto.domain.resultannouncer;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import pl.lotto.domain.resultannouncer.dto.*;
import pl.lotto.domain.resultannouncer.exceptions.ResultAnnouncerNotFoundException;
import pl.lotto.domain.resultannouncer.exceptions.TicketUUIDNotFoundException;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;
import pl.lotto.domain.resultchecker.dto.ResultCheckerResponseDto;

import java.time.Clock;
import java.time.LocalDateTime;

import static pl.lotto.domain.resultannouncer.ResultAnnouncerMapper.mapToResultLottoSaved;
import static pl.lotto.domain.resultannouncer.ResultStatus.*;

@AllArgsConstructor
public class ResultAnnouncerFacade {
    private final ResultsCheckerFacade resultsCheckerFacade;
    private final ResultAnnouncerRepository resultAnnouncerRepository;
    private final Clock clock;

    @Cacheable("results")
    public ResultAnnouncerResponseDto findResultAnnouncer(String ticketUUID) throws Exception {
        if (ticketUUID == null || ticketUUID.isEmpty()) {
            throw new TicketUUIDNotFoundException();
        }
        ResultCheckerResponseDto resultByTicketUUID = resultsCheckerFacade.findResultByTicketUUID(ticketUUID);
        ResultAnnouncerResponse resultAnnouncerResponse = resultAnnouncerRepository.findAllByTicketUUID(ticketUUID)
                .orElseThrow(() -> new ResultAnnouncerNotFoundException("Not found for ticket uuid: " + ticketUUID));
        ResultAnnouncerResponse resultSaved = resultAnnouncerRepository.save(resultAnnouncerResponse);
        ResultAnnouncerResponseDto resultLottoSaved = mapToResultLottoSaved(resultSaved);
        if (!isAfterAnnouncementTime(resultByTicketUUID)) {
            new WaitLottoMessage(resultLottoSaved, WAIT.message);
        }
        else if(isAfterAnnouncementTime(resultByTicketUUID)){
            new AlreadyCheckedLottoMessage(resultLottoSaved, ALREADY_CHECKED.message);
        }
        else if (resultLottoSaved.isWinner()) {
            new WinLottoMessage(resultLottoSaved, WIN.message);
        }
        else {
            new LoseLottoMessage(resultLottoSaved, LOSE.message);
        }

        return resultLottoSaved;
    }

    private boolean isAfterAnnouncementTime(ResultCheckerResponseDto resultResponse) {
        LocalDateTime announcementDateTime = resultResponse.drawDate();
        return LocalDateTime.now(clock).isAfter(announcementDateTime);
    }
}
