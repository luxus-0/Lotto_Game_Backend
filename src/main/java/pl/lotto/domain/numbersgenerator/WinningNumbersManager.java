package pl.lotto.domain.numbersgenerator;

import java.util.Set;
import java.util.stream.Collectors;

public class WinningNumbersManager {
    public Set<Integer> retrieveWinningNumbers(Set<Integer> randomNumbers, Set<Integer> userNumbers) {
        return randomNumbers.stream()
                .filter(userNumbers::contains)
                .collect(Collectors.toSet());
    }
}
