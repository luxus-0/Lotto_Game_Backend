package pl.lotto.domain.login.dto;

import org.hibernate.validator.constraints.UUID;

import javax.validation.constraints.NotBlank;

public record UserDto(@UUID String id,
                      @NotBlank String username,
                      @NotBlank String password) {
}
