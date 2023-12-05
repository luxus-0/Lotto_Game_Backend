package pl.lotto.infrastructure.security.token.controller.error;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.lotto.infrastructure.security.token.controller.dto.TokenErrorResponseDto;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ControllerAdvice
public class TokenControllerErrorHandler {

    public static final String BAD_CREDENTIALS = "Bad Credentials";

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public TokenErrorResponseDto handleBadCredentials(){
        return new TokenErrorResponseDto(BAD_CREDENTIALS, UNAUTHORIZED);
    }
}
