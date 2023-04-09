package pl.lotto.domain.numbersgenerator;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersGeneratorParamURLDto;

@ConfigurationProperties(prefix = "numbers.generator")
@Builder
public record WinningNumbersFacadeConfigurationProperties(String url, WinningNumbersGeneratorParamURLDto parametersUrl) {
}
