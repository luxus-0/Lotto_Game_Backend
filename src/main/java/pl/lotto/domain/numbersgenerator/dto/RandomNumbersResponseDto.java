package pl.lotto.domain.numbersgenerator.dto;

import lombok.Builder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Builder
public record RandomNumbersResponseDto(@NotNull(message = "random inputNumbers not null")
                                       @NotEmpty(message = "random inputNumbers not empty")
                                       Set<Integer> randomNumbers) {
}
