package pl.lotto.domain.numbersgenerator;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "numbers.generator")
@Builder
public record WinningNumbersFacadeConfigurationProperties(String url,  int count, int lowerBand, int upperBand) {
}
