package pl.lotto.infrastructure.numbergenerator.client;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Builder
@ConfigurationProperties(value = "connection")
public record TimeConnectionClient(int connectionTimeOut, int readTimeOut) {
}
