package pl.lotto.resultchecker;

import pl.lotto.numberreceiver.NumbersReceiverValidator;

public class ResultsCheckerFacadeConfiguration {

    ResultsCheckerFacade createModuleForTests() {
        ResultsCheckerValidator resultsCheckerValidator = new ResultsCheckerValidator();
        NumbersReceiverValidator numbersReceiverValidator = new NumbersReceiverValidator();
        ResultsCheckerRepository resultsCheckerRepository = new InMemoryResultsCheckerRepository(resultsCheckerValidator);
        return new ResultsCheckerFacade(resultsCheckerValidator, numbersReceiverValidator, resultsCheckerRepository);
    }
}
