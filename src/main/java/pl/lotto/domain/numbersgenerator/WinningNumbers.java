package pl.lotto.domain.numbersgenerator;

import lombok.Builder;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Document
public record WinningNumbers(@UUID String ticketId,
                             @NotNull(message = "winning numbers not null")
                             @NotEmpty(message = "winning numbers not empty")
                             Set<Integer> winningNumbers,
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                             LocalDateTime drawDate) {
}
