package pl.lotto.infrastructure.numbereceiver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.NumberReceiverDto;
import pl.lotto.domain.numberreceiver.dto.TicketResultDto;

import java.util.Set;

@RestController
@RequiredArgsConstructor
class NumberReceiverController {
    private final NumberReceiverFacade numberReceiverFacade;

    @PostMapping("/inputNumbers")
    ResponseEntity<TicketResultDto> inputNumbers(@RequestBody NumberReceiverDto numberReceiverDto) {
        Set<Integer> responseNumbers = numberReceiverDto.inputNumbers();
        TicketResultDto ticketResultDto = numberReceiverFacade.inputNumbers(responseNumbers);
        return ResponseEntity.ok(ticketResultDto);
    }
}
