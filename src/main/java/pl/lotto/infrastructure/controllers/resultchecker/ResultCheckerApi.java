package pl.lotto.infrastructure.controllers.resultchecker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.resultchecker.ResultsCheckerFacade;
import pl.lotto.resultchecker.dto.ResultsLottoDto;

import java.time.LocalDateTime;
import java.util.Set;

@RestController
public class ResultCheckerApi {

    private final ResultsCheckerFacade resultsCheckerFacade;

    public ResultCheckerApi(ResultsCheckerFacade resultsCheckerFacade) {
        this.resultsCheckerFacade = resultsCheckerFacade;
    }

    @GetMapping("/results")
    public ResponseEntity<ResultsLottoDto> results(@RequestBody ResultsLottoDto resultsLottoDto) {
        Set<Integer> resultNumbers = resultsLottoDto.winnerNumbers();
        LocalDateTime resultDateTime = resultsLottoDto.dateTimeDraw();
        ResultsLottoDto resultsLotto = resultsCheckerFacade.getWinnerNumbers(resultNumbers, resultDateTime);
        return new ResponseEntity<>(resultsLotto, HttpStatus.OK);
    }
}
