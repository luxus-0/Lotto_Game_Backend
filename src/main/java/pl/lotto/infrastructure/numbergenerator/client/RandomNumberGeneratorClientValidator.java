package pl.lotto.infrastructure.numbergenerator.client;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numbersgenerator.WinningNumbersConfigurationProperties;

import java.util.Set;

@AllArgsConstructor
public class RandomNumberGeneratorClientValidator {
    private final WinningNumbersConfigurationProperties properties;
    public boolean outOfRange(Set<Integer> winningNumbers) {
        return winningNumbers.stream()
                .anyMatch(number -> number < properties.lowerBand() || number > properties.upperBand());
    }

    public boolean isIncorrectSize(Set<Integer> winningNumbers){
        return winningNumbers.size() > properties.count();
    }
}
