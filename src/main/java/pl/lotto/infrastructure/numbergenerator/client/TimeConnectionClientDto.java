package pl.lotto.infrastructure.numbergenerator.client;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Builder
@ConfigurationProperties(value = "connection")
public record TimeConnectionClientDto(int connectionTimeOut, int readTimeOut) {
}
