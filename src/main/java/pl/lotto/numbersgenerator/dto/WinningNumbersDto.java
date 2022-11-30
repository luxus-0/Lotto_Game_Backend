package pl.lotto.numbersgenerator.dto;

import java.util.Set;

public class WinningNumbersDto {

    Set<Integer> winnerNumbers;

    public WinningNumbersDto(Set<Integer> winnerNumbers) {
        this.winnerNumbers = winnerNumbers;
    }
}
