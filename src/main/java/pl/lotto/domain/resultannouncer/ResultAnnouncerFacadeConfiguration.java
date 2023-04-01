package pl.lotto.domain.resultannouncer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;

import java.time.Clock;

@Configuration
public class ResultAnnouncerFacadeConfiguration {

    @Bean
    public ResultAnnouncerFacade createModuleForTest(ResultsCheckerFacade resultsCheckerFacade, ResultAnnouncerRepository resultAnnouncerRepository, Clock clock) {
        return new ResultAnnouncerFacade(resultsCheckerFacade, resultAnnouncerRepository, clock);
    }

}
