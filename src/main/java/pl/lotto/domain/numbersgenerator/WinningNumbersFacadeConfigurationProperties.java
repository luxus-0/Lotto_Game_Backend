package pl.lotto.domain.numbersgenerator;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "numbers.generator")
@Builder
public record WinningNumbersFacadeConfigurationProperties(String url_api,
                                                          int port_api,
                                                          int count,
                                                          int lowerBand,
                                                          int upperBand,
                                                          String format,
                                                          int base,
                                                          int numberColumn,
                                                          int connectionTimeOut,
                                                          int readTimeOut) {
}
