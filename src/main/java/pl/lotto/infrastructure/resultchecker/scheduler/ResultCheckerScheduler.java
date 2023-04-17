package pl.lotto.infrastructure.resultchecker.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import pl.lotto.domain.numbersgenerator.WinningNumbersFacade;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;
import pl.lotto.domain.resultchecker.dto.PlayersDto;

@AllArgsConstructor
@Log4j2
public class ResultCheckerScheduler {

    private final ResultsCheckerFacade resultsCheckerFacade;
    private final WinningNumbersFacade winningNumbersFacade;

    @Scheduled(cron = "${winners.lottery.run.occurence}")
    public PlayersDto generateWinners(){
        log.info("Winners lottery scheduler started");
        if(!winningNumbersFacade.areWinningNumbersGeneratedByDate()){
            log.error("Winning numbers are not generated");
        }
        log.info("Winning numbers are generated");
        return resultsCheckerFacade.generateResults();
    }
}
