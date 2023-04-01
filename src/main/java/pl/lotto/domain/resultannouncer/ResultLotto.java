package pl.lotto.domain.resultannouncer;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Document
@Builder
public record ResultLotto(@Id String hash, Set<Integer> numbers, Set<Integer> hitNumbers, LocalDateTime drawDate, boolean isWinner) {
}
