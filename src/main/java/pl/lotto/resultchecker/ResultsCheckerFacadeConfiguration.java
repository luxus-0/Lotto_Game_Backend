package pl.lotto.resultchecker;

import pl.lotto.numberreceiver.*;

public class ResultsCheckerFacadeConfiguration {

    ResultsCheckerFacade createModuleForTests(ResultsLottoRepository resultsRepository){
        ResultsChecker resultsChecker = new ResultsChecker();
        NumberReceiverRepository numberReceiverRepository = new InMemoryNumberReceiverRepository();
        return new ResultsCheckerFacade(resultsChecker);
    }
}
