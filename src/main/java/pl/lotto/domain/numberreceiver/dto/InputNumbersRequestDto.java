package pl.lotto.domain.numberreceiver.dto;

import lombok.Builder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Builder
public record InputNumbersRequestDto(
        @NotNull(message = "{inputNumbers.not.null")
        @NotEmpty(message = "{inputNumbers.not.empty")
        @Size(min = 1, max = 6, message = "Size must be 6!")
        Set<Integer> inputNumbers) {
}
