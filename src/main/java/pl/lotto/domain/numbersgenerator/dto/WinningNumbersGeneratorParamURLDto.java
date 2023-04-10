package pl.lotto.domain.numbersgenerator.dto;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Builder
@ConfigurationProperties(prefix = "parameters-url")
public record WinningNumbersGeneratorParamURLDto(
                                                    int count,
                                                    int lowerBand,
                                                    int upperBand,
                                                    String format,
                                                    int base,
                                                    int numberColumn) {
}
