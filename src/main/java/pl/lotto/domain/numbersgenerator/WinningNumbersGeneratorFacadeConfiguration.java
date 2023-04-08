package pl.lotto.domain.numbersgenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.infrastructure.numbergenerator.client.RandomNumberParametersUrl;

@Configuration
public class WinningNumbersGeneratorFacadeConfiguration {

    @Bean
    WinningNumbersGeneratorFacade winningNumbersGeneratorFacade(DrawDateFacade drawDateFacade, RandomNumbersGenerable generator, WinningNumbersRepository winningNumbersRepository, WinningNumbersFacadeConfigurationProperties properties) {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator(properties);
        return new WinningNumbersGeneratorFacade(drawDateFacade, generator, winningNumbersRepository, winningNumberValidator, properties);
    }

    public WinningNumbersGeneratorFacade createModuleForTest(DrawDateFacade drawDateFacade, RandomNumbersGenerable generator, WinningNumbersRepository winningNumbersRepository) {
        WinningNumbersFacadeConfigurationProperties properties = WinningNumbersFacadeConfigurationProperties.builder()
                .parametersUrl(RandomNumberParametersUrl.builder()
                        .count(6)
                        .lowerBand(1)
                        .upperBand(99)
                        .format("plain")
                        .base(10)
                        .numberColumn(1)
                        .build())
                .build();
        return winningNumbersGeneratorFacade(drawDateFacade, generator, winningNumbersRepository, properties);
    }
}
