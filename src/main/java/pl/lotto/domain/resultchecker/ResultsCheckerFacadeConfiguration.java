package pl.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numbersgenerator.WinningNumbersGeneratorFacade;

@Configuration
@AllArgsConstructor
class ResultsCheckerFacadeConfiguration {

    @Bean
    PlayerRepository playerRepository() {
        return new InMemoryPlayerDatabaseImpl();
    }

    @Bean
    ResultsCheckerFacade createModuleForTests(NumberReceiverFacade numberReceiverFacade, DrawDateFacade drawDateFacade, WinningNumbersGeneratorFacade winningNumbersGeneratorFacade, PlayerRepository playerRepository) {
        WinnersRetriever winnersRetriever = new WinnersRetriever();
        ResultValidation resultValidation = new ResultValidation();
        return new ResultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningNumbersGeneratorFacade, winnersRetriever, playerRepository, resultValidation);
    }
}
