package pl.lotto.domain.numbersgenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.infrastructure.numbergenerator.client.RandomNumberClient;

@Configuration
public class WinningNumbersGeneratorFacadeConfiguration {
    @Bean
    public WinningNumbersGeneratorFacade createModuleForTest(RandomNumberClient randomNumberClient, DrawDateFacade drawDateFacade, WinningNumbersRepository winningNumbersRepository) {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator();
        return new WinningNumbersGeneratorFacade(drawDateFacade, randomNumberClient, winningNumbersRepository, winningNumberValidator);
    }
}
