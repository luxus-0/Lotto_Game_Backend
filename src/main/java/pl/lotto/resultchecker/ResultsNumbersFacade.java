package pl.lotto.resultchecker;

import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.dto.NumbersResultMessageDto;

import java.util.Optional;
import java.util.Set;

public class ResultsNumbersFacade {
    private final ResultsChecker resultsChecker;
    private final NumberReceiverFacade receiverFacade;

    public ResultsNumbersFacade(ResultsChecker resultsChecker, NumberReceiverFacade receiverFacade) {
        this.resultsChecker = resultsChecker;
        this.receiverFacade = receiverFacade;
    }

    String messageResultChecker(Set<Integer>inputNumbers, Set<Integer> lottoNumbers){
        if(resultsChecker.checkWinnerNumbers(inputNumbers, lottoNumbers)){
            ResultsCheckerMessageProvider messageResult = new ResultsCheckerMessageProvider(resultsChecker);
            return messageResult.getResultMessage(inputNumbers, lottoNumbers);
        }
        return Optional.of("NOT WIN NUMBERS").orElseThrow();
    }
}
