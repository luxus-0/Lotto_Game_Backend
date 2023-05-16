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

    @GetMapping("/results/{ticketId}")
    ResponseEntity<ResultAnnouncerResponseDto> checkResultByTicketId(@PathVariable String ticketId) {
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.findResult(ticketId);
        return ResponseEntity.ok(resultAnnouncerResponseDto);
    }
}
