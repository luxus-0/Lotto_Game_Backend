package integration.apivalidationerror;

import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public record ApiValidationErrorDto(List<String> messages, HttpStatus status) {
}
