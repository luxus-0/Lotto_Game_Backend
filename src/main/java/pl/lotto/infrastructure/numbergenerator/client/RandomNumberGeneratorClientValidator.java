package pl.lotto.infrastructure.numbergenerator.client;

import lombok.AllArgsConstructor;
import org.springframework.web.server.ResponseStatusException;
import pl.lotto.domain.numbersgenerator.WinningNumbersConfigurationProperties;

import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
public class RandomNumberGeneratorClientValidator {

    private final WinningNumbersConfigurationProperties properties;

    public void validateRandomNumbers(int count, int lowerBand, int upperBand) {
        if (count == 0 && lowerBand < upperBand) {
            throw new ResponseStatusException(NO_CONTENT);
        }
        else if (lowerBand > upperBand && count > properties.count()) {
            throw new ResponseStatusException(NOT_FOUND);
        } else if (lowerBand == 0 && upperBand == 0 && count == 0) {
            throw new ResponseStatusException(UNAUTHORIZED);
        }
    }
}
