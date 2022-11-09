package pl.lotto.resultchecker;

import pl.lotto.numberreceiver.NumbersReceiverValidator;

public class ResultsCheckerFacadeConfiguration {

    ResultsCheckerFacade createModuleForTests(ResultsCheckerValidator validator) {
        ResultsCheckerValidator resultsCheckerValidator = new ResultsCheckerValidator();
        NumbersReceiverValidator numbersReceiverValidator = new NumbersReceiverValidator();
        ResultsCheckerRepository resultsCheckerRepository = new InMemoryResultsCheckerRepository(validator);
        return new ResultsCheckerFacade(resultsCheckerValidator, numbersReceiverValidator, resultsCheckerRepository);
    }
}
