package pl.lotto.resultchecker;


import java.time.Clock;

public class ResultsCheckerFacadeConfiguration {

    ResultsCheckerFacade createModuleForTests(Clock clock) {
        ResultsCheckerValidator resultsCheckerValidator = new ResultsCheckerValidator();
        ResultCheckerDateTime resultCheckerDateTime = new ResultCheckerDateTime(clock);
        return new ResultsCheckerFacade(resultCheckerDateTime, resultsCheckerValidator);
    }
}
