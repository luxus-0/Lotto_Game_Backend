package pl.lotto.domain.resultannouncer.dto;

import lombok.Builder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
public record PlayerDto(@NotNull @NotEmpty
                        String name,
                        @NotNull @NotEmpty
                        String surname) {
}
