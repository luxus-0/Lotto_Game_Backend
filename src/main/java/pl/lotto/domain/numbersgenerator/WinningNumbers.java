package pl.lotto.domain.numbersgenerator;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;


@Builder
public record WinningNumbers(String hash, Set<Integer> winningNumbers, LocalDateTime drawDate) {
}
