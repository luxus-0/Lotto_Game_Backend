package pl.lotto.domain.resultchecker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.winningnumbers.WinningNumbersFacade;
import pl.lotto.infrastructure.resultchecker.scheduler.ResultCheckerScheduler;

@Configuration
class ResultsCheckerFacadeConfiguration {

    @Bean
    ResultCheckerScheduler resultCheckerScheduler(ResultsCheckerFacade resultsCheckerFacade, WinningNumbersFacade winningNumbersFacade) {
        return new ResultCheckerScheduler(resultsCheckerFacade, winningNumbersFacade);
    }

    @Bean
    ResultsCheckerFacade resultsCheckerFacade(NumberReceiverFacade numberReceiverFacade, WinningNumbersFacade winningNumbersFacade, ResultCheckerRepository resultCheckerRepository) {
        Winners winners = new Winners();
        ResultCheckerValidation resultCheckerValidation = new ResultCheckerValidation();
        return new ResultsCheckerFacade(numberReceiverFacade, winningNumbersFacade, winners, resultCheckerRepository, resultCheckerValidation);
    }
}
