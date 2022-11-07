package pl.lotto.resultchecker;

public class ResultsCheckerFacadeConfiguration {

    ResultsCheckerFacade createModuleForTests() {
        ResultsCheckerValidator resultsChecker = new ResultsCheckerValidator();
        ResultsLottoRepository numberReceiverRepository = new InMemoryResultsCheckerRepository();
        return new ResultsCheckerFacade(numberReceiverRepository, resultsLottoRepository);
    }
}
