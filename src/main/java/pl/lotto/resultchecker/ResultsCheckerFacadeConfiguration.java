package pl.lotto.resultchecker;


import java.time.Clock;

public class ResultsCheckerFacadeConfiguration {

    ResultsCheckerFacade createModuleForTests(Clock clock) {
        ResultsCheckerValidator resultsCheckerValidator = new ResultsCheckerValidator();
        ResultsCheckerRepository resultsCheckerRepository = new InMemoryResultsCheckerRepository(clock);
        return new ResultsCheckerFacade(resultsCheckerValidator, resultsCheckerRepository);
    }
}
