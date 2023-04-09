package pl.lotto.infrastructure.numbergenerator.client;

import lombok.Builder;

@Builder
public record TimeConnectionClientDto(int connectionTimeOut, int readTimeOut) {
}
