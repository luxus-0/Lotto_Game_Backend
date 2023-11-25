package pl.lotto.infrastructure.numbergenerator.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.domain.numbersgenerator.WinningTicketFacade;
import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketDto;
import pl.lotto.domain.numbersgenerator.exceptions.WinnerNumbersNotFoundException;
import pl.lotto.infrastructure.numbergenerator.client.RandomNumberClient;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/number-generator/")
@Log4j2
public class NumberGeneratorController {
    private final RandomNumberClient randomNumberClient;
    private final WinningTicketFacade winningTicketFacade;

    @GetMapping
    RandomNumbersDto generateRandomNumbers() {
        return randomNumberClient.generateSixRandomNumbers();
    }

    @GetMapping("winning_numbers")
    WinningTicketDto generateWinningNumbers() throws WinnerNumbersNotFoundException {
        return winningTicketFacade.generateWinningTicket();
    }
}
