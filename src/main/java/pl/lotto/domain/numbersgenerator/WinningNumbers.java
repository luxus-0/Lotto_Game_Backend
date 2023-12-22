package pl.lotto.domain.numbersgenerator;

import lombok.Builder;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Document
public record WinningNumbers(@UUID String ticketUUID,
                             @NotNull(message = "winning inputNumbers not null")
                             @NotEmpty(message = "winning inputNumbers not empty")
                             @Min(1)
                             @Max(99)
                             Set<Integer> winningNumbers,
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                             LocalDateTime drawDate) {
}
