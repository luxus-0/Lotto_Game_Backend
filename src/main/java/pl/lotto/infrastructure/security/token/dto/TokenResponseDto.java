package pl.lotto.infrastructure.security.token.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@JsonInclude
public record TokenResponseDto(
        @NotNull(message = "Username not null")
        @NotEmpty(message = "Username not empty")
        String username,
        @NotNull(message = "Token not null")
        @NotEmpty(message = "Token not empty")
        String token) implements Serializable {
}
