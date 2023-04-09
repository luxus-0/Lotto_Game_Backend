package pl.lotto.domain.numbersgenerator.dto;

import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Builder
@ConfigurationProperties(value = "parameters-url")
public record WinningNumbersGeneratorParamURLDto(@Value("${count}") int count,
                                                 @Value("${lower_band}") int lowerBand,
                                                 @Value("${upper_band}") int upperBand,
                                                 @Value("${format}") String format,
                                                 @Value("${base}") int base,
                                                 @Value("${number_column}") int numberColumn) {
}
