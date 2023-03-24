package pl.lotto.infrastructure.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.domain.numbersgenerator.WinningNumbersGeneratorFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;

import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
class NumbersGeneratorResource {

    //private final WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;

    /*@GetMapping("/lotto_numbers")
    ResponseEntity<LottoNumbersDto> readLottoNumbers() {
        UUID uuid = UUID.randomUUID();
        Set<Integer> randomNumbersLotto = winningNumbersGeneratorFacade.generateLottoNumbers();
        return ResponseEntity.ok(new LottoNumbersDto(uuid, randomNumbersLotto));
    }

    @GetMapping("/winner_numbers")
    ResponseEntity<WinningNumbersDto> printWinnerNumbers() {
        return ResponseEntity.ok(winningNumbersGeneratorFacade.showWinnerNumbers());
    }

    @PostMapping("/user/lotto_numbers")
    ResponseEntity<LottoNumbersDto> pickLottoNumbers(NumbersGenerator numbersGenerator) {
        return ResponseEntity.ok(winningNumbersGeneratorFacade.selectLottoNumbers(numbersGenerator));
    }

    @PostMapping("/user/lotto_numbers/{uuid}")
    ResponseEntity<LottoNumbersDto> seekLottoNumbers(UUID uuid) {
        return ResponseEntity.ok(winningNumbersGeneratorFacade.findLottoNumbers(uuid));
    }*/
}
