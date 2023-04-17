package pl.lotto.domain.numbersgenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.infrastructure.numbergenerator.client.RandomNumberClient;

@Configuration
public class WinningNumbersFacadeConfiguration {

    @Bean
    RandomNumberClient randomNumberClient(RestTemplate restTemplate, WinningNumbersConfigurationProperties properties) {
        return new RandomNumberClient(restTemplate, properties);
    }

    @Bean
    public WinningNumbersFacade winningNumbersFacade(DrawDateFacade drawDateFacade, RandomNumbersGenerable generator, WinningNumbersRepository winningNumbersRepository, WinningNumbersConfigurationProperties properties) {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator(properties);
        return new WinningNumbersFacade(drawDateFacade, generator, winningNumbersRepository, winningNumberValidator);
    }

    public WinningNumbersFacade winningNumbersFacade(DrawDateFacade drawDateFacade, RandomNumbersGenerable generator, WinningNumbersRepository winningNumbersRepository) {
        WinningNumbersConfigurationProperties properties = WinningNumbersConfigurationProperties.builder()
                .url("https://random.org/integers/")
                .count(6)
                .lowerBand(1)
                .upperBand(99)
                .format("plain")
                .column(1)
                .base(10)
                .build();
        return winningNumbersFacade(drawDateFacade, generator, winningNumbersRepository, properties);
    }
}
