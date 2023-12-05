package pl.lotto.infrastructure.security.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.lotto.domain.login.User;
import pl.lotto.infrastructure.security.token.dto.TokenRequestDto;
import pl.lotto.infrastructure.security.token.dto.TokenResponseDto;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

import static java.time.ZoneOffset.UTC;

@Component
@AllArgsConstructor
public class TokenAuthenticatorFacade {

    private final AuthenticationManager authenticationManager;
    private final Clock clock;
    private final TokenConfigurationProperties properties;
    public TokenResponseDto authenticateAndGenerateToken(TokenRequestDto tokenRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(tokenRequest.username(), tokenRequest.password()));

        User user = (User) authenticate.getPrincipal();
        String token = createToken(user);
        String username = user.getUsername();

        return TokenResponseDto.builder()
                .username(username)
                .token(token)
                .build();
    }

    private String createToken(User user) {
        String secretKey = properties.secret();
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        Instant now = LocalDateTime.now(clock).toInstant(UTC);
        Instant expiresAt = now.plusMillis(properties.expirationMs());
        String issuer = properties.issuer();
        return JWT.create()
                .withSubject(user.username())
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .withIssuer(issuer)
                .sign(algorithm);
    }
}
