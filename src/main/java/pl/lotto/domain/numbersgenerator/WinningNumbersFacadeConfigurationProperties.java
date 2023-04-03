package pl.lotto.domain.numbersgenerator;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import pl.lotto.infrastructure.numbergenerator.client.QueryParametersUrl;

@ConfigurationProperties(prefix = "numbers.generator")
@Builder
public record WinningNumbersFacadeConfigurationProperties(QueryParametersUrl parametersUrl) {
}
