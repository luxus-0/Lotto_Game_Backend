package pl.lotto.resultchecker;

public class ResultsCheckerFacadeConfiguration {

    ResultsCheckerFacade createModuleForTests() {
        ResultsCheckerValidator resultsChecker = new ResultsCheckerValidator();
        ResultsCheckerRepository resultsLottoRepository = new InMemoryResultsCheckerRepository();
        return new ResultsCheckerFacade(resultsChecker, resultsLottoRepository);
    }
}
