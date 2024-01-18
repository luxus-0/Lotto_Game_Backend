package integration.numbergenerator.http;

import org.springframework.web.client.RestTemplate;
import pl.lotto.domain.randomnumbersgenerator.RandomNumbersGenerator;
import pl.lotto.domain.winningnumbers.WinningNumbersConfigurationProperties;
import pl.lotto.domain.winningnumbers.WinningNumbersFacadeConfiguration;
import pl.lotto.infrastructure.numbergenerator.client.RandomNumberGeneratorClientConfig;
import pl.lotto.infrastructure.numbergenerator.client.TimeConnectionClient;

public class RandomNumberGeneratorRestTemplateIntegrationTestConfig extends RandomNumberGeneratorClientConfig {

    WinningNumbersConfigurationProperties randomNumbersConfigurationProperties() {
        WinningNumbersFacadeConfiguration winningNumbersFacadeConfiguration = new WinningNumbersFacadeConfiguration();
        return winningNumbersFacadeConfiguration.getWinningNumbersConfigurationProperties();
    }

    RandomNumbersGenerator randomNumbersGeneratorClient(TimeConnectionClient timeConnectionClient) {
        RestTemplate restTemplate = restTemplate(timeConnectionClient, responseErrorHandlerClient());
        WinningNumbersConfigurationProperties properties = randomNumbersConfigurationProperties();
        return randomNumbersGeneratorClient(restTemplate, properties);
    }
}
