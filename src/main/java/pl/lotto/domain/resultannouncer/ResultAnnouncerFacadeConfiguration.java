package pl.lotto.domain.resultannouncer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;

import java.time.Clock;

@Configuration
class ResultAnnouncerFacadeConfiguration {

    @Bean
    public ResultAnnouncerFacade resultAnnouncerFacade(ResultsCheckerFacade resultsCheckerFacade, ResultAnnouncerRepository resultAnnouncerRepository, Clock clock) {
        return new ResultAnnouncerFacade(resultsCheckerFacade, resultAnnouncerRepository, clock);
    }

}
