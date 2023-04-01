package pl.lotto.domain.resultchecker;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record Player(String hash, Set<Integer> numbers, LocalDateTime drawDate, Set<Integer> hitNumbers, boolean isWinner, String message) {
}
