package pl.lotto.resultchecker;

class ResultsCheckerFacadeConfiguration {

    ResultsCheckerFacade createModuleForTests() {
        ResultsCheckerValidator resultsCheckerValidator = new ResultsCheckerValidator();
        return new ResultsCheckerFacade(resultsCheckerValidator);
    }
}
