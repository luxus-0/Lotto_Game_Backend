package pl.lotto.domain.resultchecker.dto;

import lombok.Builder;
import org.hibernate.validator.constraints.UUID;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record ResultDto(@UUID String ticketUUID,
                        @NotNull(message = "ticket numbers not null")
                        @NotEmpty(message = "ticket numbers not empty")
                        Set<Integer> numbers,
                        @NotBlank(message = "hit numbers not blank")
                        Set<Integer> hitNumbers,
                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                        LocalDateTime drawDate,
                        @NotBlank
                        boolean isWinner,
                        @NotBlank
                        String message) {
}
