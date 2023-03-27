package pl.lotto.domain.numbersgenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.drawdate.DrawDateFacade;

@Configuration
public class WinningNumbersGeneratorFacadeConfiguration {
    @Bean
    public WinningNumbersGeneratorFacade createModuleForTest(RandomNumberGeneratorFacade randomNumberGeneratorFacade, DrawDateFacade drawDateFacade, WinningNumbersRepository winningNumbersRepository) {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator();
        return new WinningNumbersGeneratorFacade(randomNumberGeneratorFacade, drawDateFacade, winningNumberValidator, winningNumbersRepository);
    }
}
