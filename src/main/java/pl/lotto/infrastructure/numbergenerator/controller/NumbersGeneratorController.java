package pl.lotto.infrastructure.numbergenerator.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.domain.numbersgenerator.WinningNumbersFacade;
import pl.lotto.domain.numbersgenerator.dto.RandomNumbersRequestDto;
import pl.lotto.domain.numbersgenerator.dto.RandomNumbersResponseDto;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketResponseDto;
import pl.lotto.infrastructure.numbergenerator.client.RandomNumberGeneratorClient;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Log4j2
public class NumbersGeneratorController {
    private final RandomNumberGeneratorClient randomNumberClient;
    private final WinningNumbersFacade winningNumbersFacade;

    @GetMapping("/random_numbers")
    RandomNumbersResponseDto generateRandomNumbers(@RequestBody @Valid RandomNumbersRequestDto randomNumbers) {
        return randomNumberClient.generateRandomNumbers(
                randomNumbers.count(),
                randomNumbers.lowerBand(),
                randomNumbers.lowerBand());
    }

    @GetMapping("/winning_numbers")
    WinningTicketResponseDto generateWinningNumbers() {
        return winningNumbersFacade.generateWinningNumbers();
    }
}
