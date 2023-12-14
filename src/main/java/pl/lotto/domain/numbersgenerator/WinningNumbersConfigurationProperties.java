package pl.lotto.domain.numbersgenerator;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "numbers.generator")
@Builder
public record WinningNumbersConfigurationProperties(String url, int count, int lowerBand, int upperBand, String format,
                                                    int column, int base) {
}
