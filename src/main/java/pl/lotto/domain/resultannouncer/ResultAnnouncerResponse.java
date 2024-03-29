package pl.lotto.domain.resultannouncer;

import lombok.Builder;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Document
public record ResultAnnouncerResponse(@UUID String ticketUUID,
                                      @NotNull(message = "numbers not null")
                                      @NotEmpty(message = "numbers not empty")
                                      Set<Integer> inputNumbers,
                                      @NotBlank
                                      Set<Integer> hitNumbers,
                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                      LocalDateTime drawDate,
                                      @NotBlank
                                      boolean isWinner,
                                      @NotBlank
                                      String message) implements Serializable {
}
