package pl.lotto.domain.numberreceiver;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public record Ticket(@UUID
                     String ticketUUID,
                     @NotNull(message = "inputNumbers not null")
                     @NotEmpty(message = "inputNumbers not empty")
                     Set<Integer> inputNumbers,
                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                     LocalDateTime drawDate,
                     @NotNull
                     @NotEmpty
                     String message) {
}
