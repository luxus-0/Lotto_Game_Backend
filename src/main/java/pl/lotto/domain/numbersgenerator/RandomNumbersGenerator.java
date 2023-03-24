package pl.lotto.domain.numbersgenerator;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import pl.lotto.domain.numbersgenerator.dto.RandomNumbersDto;
import pl.lotto.domain.numbersgenerator.exception.RandomNumberGeneratorClient;

@AllArgsConstructor
class RandomNumbersGenerator {

    private final RandomNumberGeneratorClient randomNumberGeneratorClient;
    void generateRandomNumbers(){
        ResponseEntity<RandomNumbersDto> sixRandomNumbers = randomNumberGeneratorClient.generateSixRandomNumbers();
        if(sixRandomNumbers.getStatusCode().is2xxSuccessful()){
            RandomNumbersDto randomNumbers = sixRandomNumbers.getBody();
        }
    }
}
