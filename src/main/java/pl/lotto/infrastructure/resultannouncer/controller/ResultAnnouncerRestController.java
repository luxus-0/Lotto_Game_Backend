package pl.lotto.infrastructure.resultannouncer.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lotto.domain.resultannouncer.ResultAnnouncerFacade;
import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;

@RestController
@AllArgsConstructor
public class ResultAnnouncerRestController {

    private final ResultAnnouncerFacade resultAnnouncerFacade;

    @GetMapping("/results/{ticketUUID}")
    ResponseEntity<ResultAnnouncerResponseDto> retrieveResultAnnouncer(@PathVariable String ticketUUID) throws Exception {
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.findResultAnnouncer(ticketUUID);
        return ResponseEntity.ok(resultAnnouncerResponseDto);
    }
}
