package pl.lotto.infrastructure.numbergenerator.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.domain.numbersgenerator.WinningTicketFacade;
import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketDto;
import pl.lotto.infrastructure.numbergenerator.client.RandomNumberGeneratorClient;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/number-generator")
@Log4j2
public class NumberGeneratorController {
    private final RandomNumberGeneratorClient randomNumberClient;
    private final WinningTicketFacade winningTicketFacade;

    @GetMapping("/random_numbers")
    RandomNumbersDto generateRandomNumbers() {
        return randomNumberClient.generateSixRandomNumbers();
    }

    @GetMapping("/winning_numbers")
    WinningTicketDto generateWinningNumbers() {
        return winningTicketFacade.generateWinningNumbers();
    }
}
