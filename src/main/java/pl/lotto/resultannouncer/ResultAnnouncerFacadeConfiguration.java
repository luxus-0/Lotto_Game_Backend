package pl.lotto.resultannouncer;

import org.springframework.context.annotation.Configuration;
import pl.lotto.resultchecker.ResultsCheckerFacade;

@Configuration
class ResultAnnouncerFacadeConfiguration {
    ResultAnnouncerFacade createModuleForTests(ResultsCheckerFacade resultsCheckerFacade) {
        return new ResultAnnouncerFacade(resultsCheckerFacade);
    }
}
