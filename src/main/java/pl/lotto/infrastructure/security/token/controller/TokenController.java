package pl.lotto.infrastructure.security.token.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.infrastructure.security.token.TokenAuthenticatorFacade;
import pl.lotto.infrastructure.security.token.dto.TokenResponseDto;
import pl.lotto.infrastructure.security.token.dto.TokenRequestDto;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class TokenController {

    private final TokenAuthenticatorFacade tokenAuthenticatorFacade;

    @PostMapping("/token")
    ResponseEntity<TokenResponseDto> authenticateAndGenerateToken(@Valid @RequestBody TokenRequestDto loginRequest){
        TokenResponseDto tokenResponse = tokenAuthenticatorFacade.authenticateAndGenerateToken(loginRequest);
        return ResponseEntity.ok(tokenResponse);
    }
}
