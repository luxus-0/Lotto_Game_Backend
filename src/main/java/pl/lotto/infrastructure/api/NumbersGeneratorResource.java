package pl.lotto.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.numbersgenerator.NumbersGenerator;
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

    @PostMapping("/user/lotto_numbers")
    ResponseEntity<LottoNumbersDto> pickLottoNumbers(NumbersGenerator numbersGenerator) {
        return ResponseEntity.ok(numbersGeneratorFacade.selectLottoNumbers(numbersGenerator));
    }

    @PostMapping("/user/lotto_numbers/{uuid}")
    ResponseEntity<LottoNumbersDto> seekLottoNumbers(UUID uuid) {
        return ResponseEntity.ok(numbersGeneratorFacade.findLottoNumbers(uuid));
    }
}
