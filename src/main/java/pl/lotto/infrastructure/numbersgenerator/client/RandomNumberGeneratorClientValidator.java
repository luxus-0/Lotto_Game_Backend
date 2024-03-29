package pl.lotto.infrastructure.numbersgenerator.client;

import lombok.AllArgsConstructor;
import org.springframework.web.server.ResponseStatusException;
import pl.lotto.domain.winningnumbers.WinningNumbersConfigurationProperties;

import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
public class RandomNumberGeneratorClientValidator {

    private final WinningNumbersConfigurationProperties properties;

    public void validateRandomNumbers(int count, int lowerBand, int upperBand) {
        if (count == 0 && lowerBand < upperBand) {
            throw new ResponseStatusException(NO_CONTENT);
        }
        else if (lowerBand > upperBand || lowerBand > properties.lowerBand() ||
                upperBand > properties.upperBand()) {
            throw new ResponseStatusException(NOT_FOUND);
        } else if (lowerBand == 0 && upperBand == 0 && count == 0) {
            throw new ResponseStatusException(UNAUTHORIZED);
        } else if(count > properties.count()){
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR);
        }
    }
}
