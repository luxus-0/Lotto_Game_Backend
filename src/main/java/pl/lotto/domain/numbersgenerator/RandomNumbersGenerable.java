package pl.lotto.domain.numbersgenerator;

import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;

import java.util.Set;

public interface RandomNumbersGenerable {
    RandomNumbersDto generateSixRandomNumbers();
    String generateUniqueTicketId();
    WinningNumbersDto generateWinnerNumbers(Set<Integer> numbers);
}
