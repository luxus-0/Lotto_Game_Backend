package pl.lotto.domain.resultannouncer;

import lombok.AllArgsConstructor;
import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import pl.lotto.domain.resultannouncer.exceptions.ResultLottoNotFoundException;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;
import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.time.Clock;
import java.time.LocalDateTime;

import static pl.lotto.domain.resultannouncer.ResultLottoMapper.mapToResultDtoSaved;
import static pl.lotto.domain.resultannouncer.ResultStatus.*;

@AllArgsConstructor
public class ResultAnnouncerFacade {
    private final ResultsCheckerFacade resultsCheckerFacade;
    private final ResultLottoRepository resultLottoRepository;
    private final Clock clock;

    public ResultAnnouncerResponseDto findResult(String ticketId) {
        if(ticketId == null){
            throw new IllegalArgumentException("Ticket id is empty");
        }
        ResultDto resultDto = resultsCheckerFacade.findResultByTicketId(ticketId);
        ResultLotto result = resultLottoRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new ResultLottoNotFoundException("Ticket ID: " +ticketId + " not found"));
        ResultLotto resultLottoSaved = resultLottoRepository.save(result);
        ResultDto resultDtoSaved = mapToResultDtoSaved(resultLottoSaved);
                if (!isAfterResultAnnouncementTime(resultDto)) {
                    return new ResultAnnouncerResponseDto(resultDtoSaved, WAIT.message);
                } else if (resultDto.isWinner()) {
                    return new ResultAnnouncerResponseDto(resultDtoSaved, WIN.message);
                } else {
                    return new ResultAnnouncerResponseDto(resultDtoSaved, LOSE.message);
                }
    }

    private boolean isAfterResultAnnouncementTime(ResultDto resultDto) {
        LocalDateTime announcementDateTime = resultDto.drawDate();
        return LocalDateTime.now(clock).isAfter(announcementDateTime);
    }
}
