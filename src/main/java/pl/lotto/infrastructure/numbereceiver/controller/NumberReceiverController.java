package pl.lotto.infrastructure.numbereceiver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.NumberReceiverRequestDto;
import pl.lotto.domain.numberreceiver.dto.TicketResponseDto;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequiredArgsConstructor
class NumberReceiverController {
    private final NumberReceiverFacade numberReceiverFacade;

    @PostMapping("/inputNumbers")
    ResponseEntity<TicketResponseDto> inputNumbers(@RequestBody @Valid NumberReceiverRequestDto numberReceiverRequestDto) {
        Set<Integer> responseNumbers = numberReceiverRequestDto.inputNumbers();
        TicketResponseDto ticketResponseDto = numberReceiverFacade.inputNumbers(responseNumbers);
        return ResponseEntity.ok(ticketResponseDto);
    }
}
