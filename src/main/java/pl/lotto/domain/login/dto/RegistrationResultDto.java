package pl.lotto.domain.login.dto;

import org.hibernate.validator.constraints.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record RegistrationResultDto(@UUID String uuid,
                                   @NotNull(message = "username not null")
                                   @NotEmpty(message = "username not empty")
                                   String username,
                                    @NotBlank
                                    boolean created) {
}
