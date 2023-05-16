package pl.lotto.infrastructure.numbergenerator.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.domain.numbersgenerator.WinningNumbersFacade;
import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;
import pl.lotto.infrastructure.numbergenerator.client.RandomNumberClient;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/number-generator/")
@Log4j2
public class NumberGeneratorController {
    private final RandomNumberClient randomNumberClient;
    private final WinningNumbersFacade winningNumbersFacade;

    @GetMapping
    RandomNumbersDto generateRandomNumbers() {
        return randomNumberClient.generateSixRandomNumbers();
    }

    @GetMapping("winning_numbers")
    WinningNumbersDto generateWinningNumbers() {
        return winningNumbersFacade.generateWinningNumbers();
    }
}
