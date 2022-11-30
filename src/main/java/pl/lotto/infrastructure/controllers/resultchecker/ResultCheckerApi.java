package pl.lotto.infrastructure.controllers.resultchecker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.resultchecker.ResultsCheckerFacade;
import pl.lotto.resultchecker.dto.ResultsLottoDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@RestController
class ResultCheckerApi {

    private final ResultsCheckerFacade resultsCheckerFacade;

    ResultCheckerApi(ResultsCheckerFacade resultsCheckerFacade) {
        this.resultsCheckerFacade = resultsCheckerFacade;
    }

    @GetMapping("/winners")
    ResponseEntity<ResultsLottoDto> readWinners(@RequestBody ResultsLottoDto resultsLottoDto) {
        Set<Integer> resultNumbers = resultsLottoDto.winnerNumbers();
        LocalDateTime resultDateTime = resultsLottoDto.dateTimeDraw();
        ResultsLottoDto resultsLotto = resultsCheckerFacade.getWinnerNumbers(resultNumbers, resultDateTime);
        return new ResponseEntity<>(resultsLotto, HttpStatus.OK);
    }

    @GetMapping("/winners/{uuid}")
    ResponseEntity<ResultsLottoDto> readWinners(@PathVariable UUID uuid) {
        ResultsLottoDto resultsLotto = resultsCheckerFacade.getWinnerNumbers(uuid);
        return new ResponseEntity<>(resultsLotto, HttpStatus.OK);
    }
}
