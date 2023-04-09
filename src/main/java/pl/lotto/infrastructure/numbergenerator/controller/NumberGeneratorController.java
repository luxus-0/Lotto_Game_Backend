package pl.lotto.infrastructure.numbergenerator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.domain.numbersgenerator.WinningNumbersGeneratorFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;

@RestController
@RequiredArgsConstructor
class NumberGeneratorController {

    private final WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;
    @GetMapping("/generate/winning_numbers")
    WinningNumbersDto generateRandomNumbers(){
        return winningNumbersGeneratorFacade.generateWinningNumbers();
    }
}
