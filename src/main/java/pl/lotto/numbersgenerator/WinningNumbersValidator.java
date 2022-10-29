package pl.lotto.numbersgenerator;

import pl.lotto.numbersgenerator.dto.WinningNumbersResultDto;

import java.util.Optional;
import java.util.Set;

import static pl.lotto.numbersgenerator.WinningNumbersMessageProvider.FAILED;
import static pl.lotto.numbersgenerator.WinningNumbersMessageProvider.SUCCESS;

class WinningNumbersValidator {

    private final NumbersGenerator numbersGenerator;

    WinningNumbersValidator(NumbersGenerator numbersGenerator) {
        this.numbersGenerator = numbersGenerator;
    }

    public WinningNumbersResultDto isWinnerNumbers(Set<Integer> numbersInputFromUser) {
        Integer numberUser = numbersInputFromUser.stream().findAny().orElse(0);
        Set<Integer> randomNumbers = numbersGenerator.generateNumbers();
        if(randomNumbers.contains(numberUser)){
            return new WinningNumbersResultDto(numbersInputFromUser, SUCCESS);
        }
       return Optional.of(new WinningNumbersResultDto(numbersInputFromUser, FAILED))
               .orElseThrow();
    }
}
