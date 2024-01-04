package pl.lotto.domain.login.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static pl.lotto.infrastructure.apivalidation.ApiValidationConstants.MESSAGE_PASSWORD;
import static pl.lotto.infrastructure.apivalidation.ApiValidationConstants.REGEX_PASSWORD;

public record RegisterUserDto(@NotNull(message = "username not null")
                              @NotEmpty(message = "username not empty")
                              String username,
                              @NotNull(message = "password not null")
                              @NotEmpty(message = "password not empty")
                              @Pattern(regexp = REGEX_PASSWORD,message = MESSAGE_PASSWORD)
                              String password) {
}
