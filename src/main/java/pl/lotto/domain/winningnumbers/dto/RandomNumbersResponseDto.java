package pl.lotto.domain.winningnumbers.dto;

import lombok.Builder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Builder
public record RandomNumbersResponseDto(@NotNull(message = "random numbers not null")
                                       @NotEmpty(message = "random numbers not empty")
                                       Set<Integer> randomNumbers) implements Serializable {
}
