package pl.lotto.infrastructure.resultchecker.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import pl.lotto.domain.numbersgenerator.WinningTicketFacade;
import pl.lotto.domain.numbersgenerator.exceptions.WinnerNumbersNotFoundException;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;

@AllArgsConstructor
@Log4j2
public class ResultCheckerScheduler {

    private final ResultsCheckerFacade resultsCheckerFacade;
    private final WinningTicketFacade winningTicketFacade;

    @Scheduled(cron = "${results.checker.lottery.run.occurence}")
    public void generateWinners() throws WinnerNumbersNotFoundException {
        log.info("Winners lottery scheduler started");
        if (!winningTicketFacade.areWinningNumbersGeneratedByDate()) {
            log.error("Winning numbers are not generated");
        }
        log.info("Winning numbers are generated");
        resultsCheckerFacade.generateResults();
    }
}
