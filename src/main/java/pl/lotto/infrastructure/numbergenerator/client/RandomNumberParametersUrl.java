package pl.lotto.infrastructure.numbergenerator.client;

import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;

@Builder
public record RandomNumberParametersUrl(
        @Value("${numbers.generator.url}") String url,
        @Value("${numbers.generator.count}") int count,
        @Value("${numbers.generator.lower_band}") int lowerBand,
        @Value("${numbers.generator.upper_band}") int upperBand,
        @Value("${numbers.generator.format}") String format,
        @Value("${numbers.generator.number_column}") int numberColumn,
        @Value("${numbers.generator.base}") int base) {
}
