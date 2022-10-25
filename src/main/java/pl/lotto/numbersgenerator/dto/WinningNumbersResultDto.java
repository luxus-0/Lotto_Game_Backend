package pl.lotto.numbersgenerator.dto;

import java.util.Set;

public record WinningNumbersResultDto(Set<Integer> winnerNumbers, String winnerMessage) {
}
