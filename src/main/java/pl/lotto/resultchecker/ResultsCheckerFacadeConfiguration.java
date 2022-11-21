package pl.lotto.resultchecker;

import java.time.Clock;

class ResultsCheckerFacadeConfiguration {
    ResultsCheckerFacade createModuleForTests(Clock clock) {
        ResultsCheckerValidator resultsCheckerValidator = new ResultsCheckerValidator();
        NumberGenerator numberGenerator = new NumberGenerator();
        return new ResultsCheckerFacade(resultsCheckerValidator, numberGenerator, clock);
    }
}
