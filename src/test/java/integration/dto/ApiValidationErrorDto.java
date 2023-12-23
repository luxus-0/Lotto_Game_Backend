package integration.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiValidationErrorDto(@JsonProperty("messages")List<String> messages,@JsonProperty("status") HttpStatus status) {
}
