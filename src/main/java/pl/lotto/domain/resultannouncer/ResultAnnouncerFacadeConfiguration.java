package pl.lotto.domain.resultannouncer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.drawdate.AdjustableClock;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.time.ZoneOffset.UTC;

@Configuration
public class ResultAnnouncerFacadeConfiguration {

    @Bean
    AdjustableClock clock() {
        return new AdjustableClock(LocalDateTime.of(2022, 11, 19, 12, 0, 0).toInstant(UTC), ZoneId.systemDefault());
    }

    @Bean
    public ResultAnnouncerFacade resultAnnouncerFacade(ResultsCheckerFacade resultsCheckerFacade, ResultLottoRepository resultLottoRepository, Clock clock) {
        return new ResultAnnouncerFacade(resultsCheckerFacade, resultLottoRepository, clock);
    }

}
