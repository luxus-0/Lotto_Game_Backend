package pl.lotto.domain.login.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.infrastructure.security.token.JwtAuthenticatorFacade;
import pl.lotto.infrastructure.security.token.dto.TokenRequestDto;
import pl.lotto.infrastructure.security.token.dto.TokenResponseDto;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
public class LoginController {

    private final JwtAuthenticatorFacade jwtAuthenticatorFacade;
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid TokenRequestDto tokenRequest) {
        TokenResponseDto jwtResponse = jwtAuthenticatorFacade.authenticateAndGenerateToken(tokenRequest);
        return ResponseEntity.status(CREATED).body(jwtResponse);
    }
}
