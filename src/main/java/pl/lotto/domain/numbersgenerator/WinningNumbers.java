package pl.lotto.domain.numbersgenerator;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Document
@Builder
public record WinningNumbers(String hash, Set<Integer> winningNumbers, LocalDateTime drawDate, String validationMessage) {
}
