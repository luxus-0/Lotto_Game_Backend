package pl.lotto.domain.resultannouncer;

import lombok.AllArgsConstructor;
import pl.lotto.domain.resultannouncer.dto.*;
import pl.lotto.domain.resultannouncer.exceptions.ResultAnnouncerNotFoundException;
import pl.lotto.domain.resultannouncer.exceptions.TicketUUIDNotFoundException;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;
import pl.lotto.domain.resultchecker.dto.ResultResponseDto;

import java.time.Clock;
import java.time.LocalDateTime;

import static pl.lotto.domain.resultannouncer.ResultAnnouncerMapper.mapToResultAnnouncerResponseDto;
import static pl.lotto.domain.resultannouncer.ResultStatus.*;

@AllArgsConstructor
public class ResultAnnouncerFacade {
    private final ResultsCheckerFacade resultsCheckerFacade;
    private final ResultAnnouncerRepository resultAnnouncerRepository;
    private final Clock clock;

    public ResultAnnouncerResponseDto findResult(String ticketUUID) throws Exception {
        if (ticketUUID == null || ticketUUID.isEmpty()) {
            throw new TicketUUIDNotFoundException();
        }
        ResultResponseDto resultResponseDto = resultsCheckerFacade.findResultByTicketUUID(ticketUUID);
        ResultAnnouncerResponse resultAnnouncerResponse = resultAnnouncerRepository.findAllByTicketUUID(ticketUUID)
                .orElseThrow(() -> new ResultAnnouncerNotFoundException("Not found for ticket id: " + ticketUUID));
        ResultAnnouncerResponse resultSaved = resultAnnouncerRepository.save(resultAnnouncerResponse);
        ResultAnnouncerResponseDto resultSavedDto = mapToResultAnnouncerResponseDto(resultSaved);
        if (!isAfterAnnouncementTime(resultResponseDto)) {
            new WaitLottoMessage(resultSavedDto, WAIT.message);
        }
        else if(isAfterAnnouncementTime(resultResponseDto)){
            new AlreadyCheckedLottoMessage(resultSavedDto, ALREADY_CHECKED.message);
        }
        else if (resultSavedDto.isWinner()) {
            new WinLottoMessage(resultSavedDto, WIN.message);
        }
        else {
            new LoseLottoMessage(resultSavedDto, LOSE.message);
        }

        return resultSavedDto;
    }

    private boolean isAfterAnnouncementTime(ResultResponseDto resultResponse) {
        LocalDateTime announcementDateTime = resultResponse.drawDate();
        return LocalDateTime.now(clock).isAfter(announcementDateTime);
    }
}
