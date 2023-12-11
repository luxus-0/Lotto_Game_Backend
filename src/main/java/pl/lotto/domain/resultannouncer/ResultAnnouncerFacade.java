package pl.lotto.domain.resultannouncer;

import lombok.AllArgsConstructor;
import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import pl.lotto.domain.resultannouncer.exceptions.ResultAnnouncerNotFoundException;
import pl.lotto.domain.resultannouncer.exceptions.TicketUUIDNotFoundException;
import pl.lotto.domain.resultchecker.ResultCheckerResponse;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;
import pl.lotto.domain.resultchecker.dto.ResultCheckerResponseDto;

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
        ResultCheckerResponseDto resultCheckerResponseDto = resultsCheckerFacade.findResultByTicketUUID(ticketUUID);
        ResultAnnouncerResponse resultAnnouncerResponse = resultAnnouncerRepository.findAllByTicketUUID(ticketUUID)
                .orElseThrow(() -> new ResultAnnouncerNotFoundException("Not found for ticket id: " + ticketUUID));
        ResultAnnouncerResponse resultAnnouncerResponseSaved = resultAnnouncerRepository.save(resultAnnouncerResponse);
        ResultCheckerResponse resultCheckerResponse = resultCheckerResponseDto.ticketsWinningNumbersSaved().stream()
                .findAny()
                .orElseThrow(() -> new RuntimeException(TICKET_NOT_FOUND));
        ResultAnnouncerResponseDto toResultAnnouncerResponseSavedDto = mapToResultAnnouncerResponseDto(resultAnnouncerResponseSaved);
        if (!isAfterResultAnnouncementTime(resultCheckerResponse)) {
            return toWaitMessageResult(toResultAnnouncerResponseSavedDto);
        } else if (toResultAnnouncerResponseSavedDto.isWinner()) {
            toWinResult(toResultAnnouncerResponseSavedDto);
        } else {
            return toLoseResult(toResultAnnouncerResponseSavedDto);
        }
        throw new ResultAnnouncerNotFoundException(TICKET_NOT_FOUND);
    }

    private boolean isAfterResultAnnouncementTime(ResultCheckerResponse resultCheckerResponse) {
        LocalDateTime announcementDateTime = resultCheckerResponse.drawDate();
        return LocalDateTime.now(clock).isAfter(announcementDateTime);
    }
}
