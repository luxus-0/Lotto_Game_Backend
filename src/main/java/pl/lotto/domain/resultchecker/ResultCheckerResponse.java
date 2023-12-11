package pl.lotto.domain.resultchecker;

import lombok.Builder;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Document
public record ResultCheckerResponse(@UUID String ticketUUID,
                                    @NotNull(message = "input numbers not null")
                                   @NotEmpty(message = "input numbers not empty")
                                   Set<Integer> inputNumbers,
                                    @NotBlank
                                   Set<Integer> hitNumbers,
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                    LocalDateTime drawDate,
                                    @NotEmpty
                            @NotNull
                            boolean isWinner,
                                    @NotNull
                            @NotEmpty
                            String message) {
}
