package pl.lotto.infrastructure.resultannouncer.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.domain.resultannouncer.ResultAnnouncerFacade;
import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;

@RestController
@AllArgsConstructor
public class ResultAnnouncerRestController {

    private final ResultAnnouncerFacade resultAnnouncerFacade;

    @GetMapping("/results/{ticketUUID}")
    ResponseEntity<ResultAnnouncerResponseDto> checkResultsByTicketId(@PathVariable String ticketUUID) throws Exception {
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.findResult(ticketUUID);
        return ResponseEntity.ok(resultAnnouncerResponseDto);
    }
}
