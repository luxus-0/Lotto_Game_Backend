package pl.lotto.infrastructure.numbersgenerator.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.domain.winningnumbers.dto.RandomNumbersRequestDto;
import pl.lotto.domain.winningnumbers.dto.RandomNumbersResponseDto;
import pl.lotto.infrastructure.numbersgenerator.client.RandomNumberGeneratorClient;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Log4j2
public class RandomNumbersGeneratorController {
    private final RandomNumberGeneratorClient randomNumberClient;

    @GetMapping("/random_numbers")
    ResponseEntity<RandomNumbersResponseDto> generateRandomNumbers(@RequestBody @Valid RandomNumbersRequestDto randomNumbers) {
        return ResponseEntity.ok(randomNumberClient.generateRandomNumbers(
                randomNumbers.count(),
                randomNumbers.lowerBand(),
                randomNumbers.lowerBand()));
    }
}
