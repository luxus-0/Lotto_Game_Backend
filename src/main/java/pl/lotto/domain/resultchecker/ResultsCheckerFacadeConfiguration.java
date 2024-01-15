package pl.lotto.domain.resultchecker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numbersgenerator.WinningNumbersFacade;
import pl.lotto.infrastructure.resultchecker.scheduler.ResultCheckerScheduler;

@Configuration
class ResultsCheckerFacadeConfiguration {

    @Bean
    ResultCheckerScheduler resultCheckerScheduler(ResultsCheckerFacade resultsCheckerFacade, WinningNumbersFacade winningNumbersFacade) {
        return new ResultCheckerScheduler(resultsCheckerFacade, winningNumbersFacade);
    }

    @Bean
    ResultsCheckerFacade resultsCheckerFacade(NumberReceiverFacade numberReceiverFacade, DrawDateFacade drawDateFacade, WinningNumbersFacade winningNumbersFacade, ResultCheckerRepository resultCheckerRepository) {
        WinnersRetriever winnersRetriever = new WinnersRetriever();
        ResultCheckerValidation resultCheckerValidation = new ResultCheckerValidation();
        return new ResultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningNumbersFacade, winnersRetriever, resultCheckerRepository, resultCheckerValidation);
    }
}
