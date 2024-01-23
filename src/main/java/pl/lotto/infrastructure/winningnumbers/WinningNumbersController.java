package pl.lotto.infrastructure.winningnumbers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.domain.numberreceiver.exceptions.WinningTicketNotFoundException;
import pl.lotto.domain.winningnumbers.WinningNumbersFacade;
import pl.lotto.domain.winningnumbers.dto.WinningTicketResponseDto;

@RestController
@RequiredArgsConstructor
@Log4j2
public class WinningNumbersController {

    private final WinningNumbersFacade winningNumbersFacade;
    @GetMapping("/winning_numbers")
    ResponseEntity<WinningTicketResponseDto> generateWinningNumbers() throws WinningTicketNotFoundException {
        return ResponseEntity.ok(winningNumbersFacade.generateWinningNumbers());
    }
}
