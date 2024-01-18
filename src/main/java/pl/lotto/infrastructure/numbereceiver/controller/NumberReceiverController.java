package pl.lotto.infrastructure.numbereceiver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.InputNumbersRequestDto;
import pl.lotto.domain.numberreceiver.dto.TicketResponseDto;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
class NumberReceiverController {
    private final NumberReceiverFacade numberReceiverFacade;

    @PostMapping("/inputNumbers")
    ResponseEntity<TicketResponseDto> inputNumbers(@RequestBody @Valid InputNumbersRequestDto inputNumbersRequest) throws Exception {
        TicketResponseDto ticketResponse = numberReceiverFacade.inputNumbers(inputNumbersRequest);
        return ResponseEntity.ok(ticketResponse);
    }
}
