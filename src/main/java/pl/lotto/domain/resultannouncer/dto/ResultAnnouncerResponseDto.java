package pl.lotto.domain.resultannouncer.dto;

import lombok.Builder;
import org.hibernate.validator.constraints.UUID;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record ResultAnnouncerResponseDto(@UUID
                                         String ticketUUID,
                                         Set<Integer> inputNumbers,
                                         @NotNull
                                         @NotEmpty
                                         Set<Integer> hitNumbers,
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                         LocalDateTime drawDate,
                                         boolean isWinner,
                                         String message) implements Serializable {
}
