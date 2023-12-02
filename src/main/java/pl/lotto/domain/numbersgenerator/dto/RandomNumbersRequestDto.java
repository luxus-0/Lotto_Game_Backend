package pl.lotto.domain.numbersgenerator.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public record RandomNumbersRequestDto(
        @NotBlank @Min(value = 1) int count,
        @NotBlank @Min(value = 1) int lowerBand,
        @NotBlank @Max(value = 99) int upperBand) {
}
