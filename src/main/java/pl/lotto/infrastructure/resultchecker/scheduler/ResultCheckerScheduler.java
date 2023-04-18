package pl.lotto.infrastructure.resultchecker.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import pl.lotto.domain.numbersgenerator.WinningNumbersFacade;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;

@AllArgsConstructor
@Log4j2
public class ResultCheckerScheduler {

    private final ResultsCheckerFacade resultsCheckerFacade;
    private final WinningNumbersFacade winningNumbersFacade;

    @Scheduled(cron = "${winners.lottery.run.occurence}")
    public void generateWinners(){
        log.info("Winners lottery scheduler started");
        if(!winningNumbersFacade.areWinningNumbersGeneratedByDate()){
            log.error("Winning numbers are not generated");
        }
        log.info("Winning numbers are generated");
        log.info(resultsCheckerFacade.generateResults());
    }
}
