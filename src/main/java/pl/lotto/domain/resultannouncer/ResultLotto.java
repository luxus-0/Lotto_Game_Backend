package pl.lotto.domain.resultannouncer;

import lombok.Builder;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Document
public record ResultLotto(@UUID String ticketId,
                          Set<Integer> numbers,
                          Set<Integer> hitNumbers,
                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                          LocalDateTime drawDate,
                          boolean isWinner,
                          String message) {
}
