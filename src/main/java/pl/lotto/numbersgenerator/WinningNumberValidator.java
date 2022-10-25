package pl.lotto.numbersgenerator;

import pl.lotto.numbersgenerator.dto.WinningNumbersResultDto;

import java.util.Set;

class WinningNumberValidator{

    public WinningNumbersResultDto isWinnerNumbers(Set<Integer> clientNumbers, Set<Integer> randomNumbers) {
         int numberClient = readClientNumber(clientNumbers);
         if(randomNumbers.contains(numberClient)){
             return new WinningNumbersResultDto(randomNumbers, "YOU WIN");
         }
        return new WinningNumbersResultDto(randomNumbers, "YOU LOSE");
    }

    private static int readClientNumber(Set<Integer> clientNumbers) {
        return clientNumbers.stream()
                .mapToInt(Integer::intValue)
                .findAny()
                .orElse(0);
    }
}
