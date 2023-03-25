package pl.lotto.domain.numbersgenerator;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;

import java.time.LocalDateTime;
import java.util.Set;

@Document
@Builder
public record WinningNumbers(String hash, Set<Integer> winningNumbers, LocalDateTime date) {
}
