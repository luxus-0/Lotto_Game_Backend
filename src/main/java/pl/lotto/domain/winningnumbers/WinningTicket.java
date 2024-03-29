package pl.lotto.domain.winningnumbers;

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
public record WinningTicket(@UUID
                            @NotNull
                            String ticketUUID,
                            @NotNull
                            @NotEmpty
                            Set<Integer> inputNumbers,
                            @NotNull
                            @NotEmpty
                            Set<Integer> hitNumbers,
                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                            LocalDateTime drawDate,
                            boolean isWinner,
                            @NotNull
                            @NotEmpty
                            String message) {
}
