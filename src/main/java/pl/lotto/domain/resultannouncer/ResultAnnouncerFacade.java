package pl.lotto.domain.resultannouncer;

import lombok.AllArgsConstructor;
import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import pl.lotto.domain.resultannouncer.exceptions.ResultAnnouncerNotFoundException;
import pl.lotto.domain.resultannouncer.exceptions.TicketUUIDNotFoundException;
import pl.lotto.domain.resultchecker.TicketResults;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;
import pl.lotto.domain.resultchecker.dto.ResultResponseDto;

import java.time.Clock;
import java.time.LocalDateTime;

import static pl.lotto.domain.resultannouncer.ResultAnnouncerMapper.*;
import static pl.lotto.domain.resultchecker.ResultCheckerMessageProvider.TICKET_NOT_FOUND;

@AllArgsConstructor
public class ResultAnnouncerFacade {
    private final ResultsCheckerFacade resultsCheckerFacade;
    private final ResultAnnouncerRepository resultAnnouncerRepository;
    private final Clock clock;

    public ResultAnnouncerResponseDto findResult(String ticketUUID) throws Exception {
        if (ticketUUID == null) {
            throw new TicketUUIDNotFoundException();
        }
        ResultResponseDto resultResponseDto = resultsCheckerFacade.findResultByTicketUUID(ticketUUID);
        ResultAnnouncerResponse resultAnnouncerResponse = resultAnnouncerRepository.findAllByTicketUUID(ticketUUID)
                .orElseThrow(() -> new ResultAnnouncerNotFoundException("Not found for ticket id: " + ticketUUID));
        ResultAnnouncerResponse resultAnnouncerResponseSaved = resultAnnouncerRepository.save(resultAnnouncerResponse);
        ResultAnnouncerResponseDto toResultAnnouncerResponseSavedDto = mapToResultAnnouncerResponseDto(resultAnnouncerResponseSaved);
        if (!isAfterResultAnnouncementTime(resultResponseDto)) {
            return toWaitMessageResult(toResultAnnouncerResponseSavedDto);
        } else if (toResultAnnouncerResponseSavedDto.isWinner()) {
            toWinResult(toResultAnnouncerResponseSavedDto);
        } else {
            return toLoseResult(toResultAnnouncerResponseSavedDto);
        }
        throw new ResultAnnouncerNotFoundException(TICKET_NOT_FOUND);
    }

    private boolean isAfterResultAnnouncementTime(ResultResponseDto resultResponse) {
        LocalDateTime announcementDateTime = resultResponse.drawDate();
        return LocalDateTime.now(clock).isAfter(announcementDateTime);
    }
}
