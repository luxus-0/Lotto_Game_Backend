package pl.lotto.infrastructure.security.token.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record TokenRequestDto(@NotNull(message = "username not null")
                              @NotEmpty(message = "username not empty")
                              String username,
                              @NotNull(message = "password not null")
                              @NotEmpty(message = "password not empty")
                              String password) {
}
