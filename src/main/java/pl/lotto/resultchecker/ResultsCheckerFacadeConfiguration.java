package pl.lotto.resultchecker;

import java.time.Clock;

class ResultsCheckerFacadeConfiguration {
    ResultsCheckerFacade createModuleForTests(Clock clock) {
        ResultsCheckerValidator resultsCheckerValidator = new ResultsCheckerValidator();
        return new ResultsCheckerFacade(resultsCheckerValidator, clock);
    }
}
