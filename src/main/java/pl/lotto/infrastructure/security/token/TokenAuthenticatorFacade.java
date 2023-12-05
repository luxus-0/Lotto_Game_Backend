package pl.lotto.infrastructure.security.token;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.lotto.infrastructure.security.token.dto.TokenRequestDto;
import pl.lotto.infrastructure.security.token.dto.TokenResponseDto;

@Component
@AllArgsConstructor
public class TokenAuthenticatorFacade {

    private final AuthenticationManager authenticationManager;
    public TokenResponseDto authenticateAndGenerateToken(TokenRequestDto tokenRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(tokenRequest.username(), tokenRequest.password()));

        return TokenResponseDto.builder()
                .build();
    }
}
