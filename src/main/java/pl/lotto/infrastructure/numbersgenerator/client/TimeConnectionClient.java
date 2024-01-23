package pl.lotto.infrastructure.numbersgenerator.client;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Builder
@ConfigurationProperties(value = "connection")
public record TimeConnectionClient(int connectionTimeOut, int readTimeOut) {
}
