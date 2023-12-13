package pl.lotto.domain.numberreceiver.dto;

import lombok.Builder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Builder
public record InputNumbersRequestDto(
        @NotNull(message = "{inputNumbers.not.null")
        @NotEmpty(message = "{inputNumbers.not.empty")
        Set<Integer> inputNumbers) {
}
