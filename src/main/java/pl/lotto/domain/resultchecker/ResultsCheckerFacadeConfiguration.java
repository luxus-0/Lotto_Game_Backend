package pl.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.drawdate.AdjustableClock;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numbersgenerator.WinningNumbersFacade;
import pl.lotto.infrastructure.resultchecker.scheduler.ResultCheckerScheduler;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.time.ZoneOffset.UTC;

@Configuration
@AllArgsConstructor
class ResultsCheckerFacadeConfiguration {

    @Bean
    Clock clock(){
        return new AdjustableClock(LocalDateTime.of(2022, 11, 19, 12,0,0).toInstant(UTC), ZoneId.systemDefault());
    }

    @Bean
    ResultCheckerScheduler resultCheckerScheduler(ResultsCheckerFacade resultsCheckerFacade, WinningNumbersFacade winningNumbersFacade) {
        return new ResultCheckerScheduler(resultsCheckerFacade, winningNumbersFacade);
    }

    @Bean
    ResultsCheckerFacade resultsCheckerFacade(NumberReceiverFacade numberReceiverFacade, DrawDateFacade drawDateFacade, WinningNumbersFacade winningNumbersFacade, PlayerRepository playerRepository) {
        WinnersRetriever winnersRetriever = new WinnersRetriever();
        ResultValidation resultValidation = new ResultValidation();
        return new ResultsCheckerFacade(numberReceiverFacade, drawDateFacade, winningNumbersFacade, winnersRetriever, playerRepository, resultValidation);
    }
}
