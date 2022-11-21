package pl.lotto.resultchecker;

class ResultsCheckerFacadeConfiguration {

    ResultsCheckerFacade createModuleForTests() {
        ResultsCheckerValidator resultsCheckerValidator = new ResultsCheckerValidator();
        NumberGenerator numberGenerator = new NumberGenerator();
        return new ResultsCheckerFacade(resultsCheckerValidator, numberGenerator);
    }
}
