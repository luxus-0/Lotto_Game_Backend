package pl.lotto.numbersgenerator;

import pl.lotto.numbersgenerator.dto.WinningNumbersResultDto;

import java.util.Set;

import static pl.lotto.numbersgenerator.WinningNumbersMessageProvider.FAILED;
import static pl.lotto.numbersgenerator.WinningNumbersMessageProvider.SUCCESS;

public class WinningNumbersFacade {
    private final WinningNumbersValidator winningNumbersValidator;

    public WinningNumbersFacade(WinningNumbersValidator winningNumbersValidator, NumbersGenerator numbersGenerator) {
        this.winningNumbersValidator = winningNumbersValidator;
    }

    WinningNumbersResultDto checkWinnerNumbers(Set<Integer> userInputNumbers){
        WinningNumbersResultDto validator = winningNumbersValidator.isWinnerNumbers(userInputNumbers);
        Set<Integer> numbers = validator.winnerNumbers();
        if(numbers.isEmpty()){
            return new WinningNumbersResultDto(numbers, FAILED);
        }
        return new WinningNumbersResultDto(numbers, SUCCESS);

    }

}
