package pl.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numbersgenerator.WinningNumbersFacade;

@Configuration
@AllArgsConstructor
class ResultsCheckerFacadeConfiguration {

    @Bean
    ResultsCheckerFacade resultsCheckerFacade(NumberReceiverFacade numberReceiverFacade, DrawDateFacade drawDateFacade, WinningNumbersFacade winningNumbersFacade, PlayerRepository playerRepository) {
        WinnersRetriever winnersRetriever = new WinnersRetriever();
        ResultValidation resultValidation = new ResultValidation();
        return new ResultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningNumbersFacade, winnersRetriever, playerRepository, resultValidation);
    }
}
