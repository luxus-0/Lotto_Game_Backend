package pl.lotto.infrastructure.numbereceiver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.InputNumbersRequestDto;
import pl.lotto.domain.numberreceiver.dto.InputNumbersResponseDto;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequiredArgsConstructor
class NumberReceiverController {
    private final NumberReceiverFacade numberReceiverFacade;

    @PostMapping("/inputNumbers")
    ResponseEntity<InputNumbersResponseDto> inputNumbers(@RequestBody @Valid InputNumbersRequestDto inputNumbersRequest) {
        InputNumbersResponseDto ticketResponse = numberReceiverFacade.inputNumbers(inputNumbersRequest);
        return ResponseEntity.ok(ticketResponse);
    }
}
