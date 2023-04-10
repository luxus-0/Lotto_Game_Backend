package pl.lotto.domain.numbersgenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.infrastructure.numbergenerator.client.RandomNumberClient;

@Configuration
public class WinningNumbersGeneratorFacadeConfiguration {

    @Bean
    RandomNumbersGenerable randomNumbersGenerable(RestTemplate restTemplate, WinningNumbersFacadeConfigurationProperties properties) {
        return new RandomNumberClient(restTemplate, properties);
    }

    @Bean
    public WinningNumbersGeneratorFacade winningNumbersGeneratorFacade(DrawDateFacade drawDateFacade, RandomNumbersGenerable generator, WinningNumbersRepository winningNumbersRepository, WinningNumbersFacadeConfigurationProperties properties) {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator(properties);
        return new WinningNumbersGeneratorFacade(drawDateFacade, generator, winningNumbersRepository, winningNumberValidator, properties);
    }
    
    public WinningNumbersGeneratorFacade winningNumbersGeneratorFacade(DrawDateFacade drawDateFacade, RandomNumbersGenerable generator, WinningNumbersRepository winningNumbersRepository) {
        WinningNumbersFacadeConfigurationProperties properties = WinningNumbersFacadeConfigurationProperties.builder()
                .url("https://random.org/integers/")
                .count(6)
                .lowerBand(1)
                .upperBand(99)
                .build();
        return winningNumbersGeneratorFacade(drawDateFacade, generator, winningNumbersRepository, properties);
    }
}
