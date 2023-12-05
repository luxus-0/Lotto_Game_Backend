package pl.lotto.infrastructure.security.token.dto;

import lombok.Builder;

@Builder
public record TokenResponseDto(String username,
                               String token) {
}
