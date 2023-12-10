package pl.lotto.domain.resultchecker;

import lombok.Builder;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Document
public record Player(@UUID String ticketUUID,
                     @NotBlank
                     Set<Integer> numbers,
                     @NotBlank
                     Set<Integer> hitNumbers,
                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                     LocalDateTime drawDate,
                     @NotBlank
                     boolean isWinner,
                     @NotBlank
                     String message) {
}
