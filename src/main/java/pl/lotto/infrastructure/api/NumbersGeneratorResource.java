package pl.lotto.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.numbersgenerator.NumbersGeneratorFacade;
import pl.lotto.numbersgenerator.dto.LottoNumbersDto;
import pl.lotto.numbersgenerator.dto.WinningNumbersDto;

import java.util.Set;
import java.util.UUID;

@RestController
class NumbersGeneratorResource {

    private final NumbersGeneratorFacade numbersGeneratorFacade;

    NumbersGeneratorResource(NumbersGeneratorFacade numbersGeneratorFacade) {
        this.numbersGeneratorFacade = numbersGeneratorFacade;
    }

    @GetMapping("/lotto_numbers")
    ResponseEntity<LottoNumbersDto> readLottoNumbers() {
        UUID uuid = UUID.randomUUID();
        Set<Integer> randomNumbersLotto = numbersGeneratorFacade.generateLottoNumbers();
        return ResponseEntity.ok(new LottoNumbersDto(uuid, randomNumbersLotto));
    }

    @GetMapping("/winner_numbers")
    ResponseEntity<WinningNumbersDto> printWinnerNumbers() {
        return ResponseEntity.ok(numbersGeneratorFacade.showWinnerNumbers());
    }
}
