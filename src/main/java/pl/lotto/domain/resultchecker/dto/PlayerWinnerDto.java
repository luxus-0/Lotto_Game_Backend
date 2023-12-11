package pl.lotto.domain.resultchecker.dto;

import lombok.Builder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
public record PlayerWinnerDto(@NotNull @NotEmpty
                              String name,
                              @NotNull @NotEmpty
                              String surname) {
}
