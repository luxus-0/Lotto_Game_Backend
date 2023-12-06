package pl.lotto.domain.login.dto;

import javax.validation.constraints.NotBlank;

public record RegisterUserDto(@NotBlank String username, @NotBlank String password) {
}
