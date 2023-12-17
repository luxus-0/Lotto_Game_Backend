package pl.lotto.infrastructure.security.token.dto;

import lombok.Builder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
public record TokenResponseDto(
        @NotNull(message = "Username not null")
        @NotEmpty(message = "Username not empty")
        String username,
        @NotNull(message = "Token not null")
        @NotEmpty(message = "Token not empty")
        String token) {
}
