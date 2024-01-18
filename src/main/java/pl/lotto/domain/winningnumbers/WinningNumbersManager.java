package pl.lotto.domain.winningnumbers;

import java.util.Set;
import java.util.stream.Collectors;

public class WinningNumbersManager {
    public Set<Integer> retrieveWinningNumbers(Set<Integer> randomNumbers, Set<Integer> userNumbers) {
        return randomNumbers.stream()
                .filter(userNumbers::contains)
                .collect(Collectors.toSet());
    }
}
