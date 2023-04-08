package pl.lotto.domain.resultchecker;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record Ticket(String hash, Set<Integer> numbers, LocalDateTime drawDate) {
}
