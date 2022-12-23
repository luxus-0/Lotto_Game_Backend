package pl.lotto.numbersgenerator;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.lotto.numbersgenerator.dto.LottoNumbersDto;
import pl.lotto.numbersgenerator.dto.WinningNumbersDto;

import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static pl.lotto.numbersgenerator.WinningNumbersMessageProvider.winningNumbersNotFound;

@Service
@Log4j2
public class NumbersGeneratorFacade {

    private static final Integer MIN_NUMBER = 1;
    private static final Integer MAX_NUMBER = 99;
    private final NumbersGeneratorValidator numbersGeneratorValidator;
    private final NumbersGeneratorRepositoryImpl numbersGeneratorRepositoryImpl;

    public NumbersGeneratorFacade(NumbersGeneratorValidator numbersGeneratorValidator, NumbersGeneratorRepositoryImpl numbersGeneratorRepositoryImpl) {
        this.numbersGeneratorValidator = numbersGeneratorValidator;
        this.numbersGeneratorRepositoryImpl = numbersGeneratorRepositoryImpl;
    }

    public Set<Integer> generateLottoNumbers() {
        return IntStream.rangeClosed(MIN_NUMBER, MAX_NUMBER)
                .map(randomNumbers -> new Random().nextInt())
                .boxed()
                .collect(Collectors.toSet());
    }

    public WinningNumbersDto showWinnerNumbers() {
        Set<Integer> lottoNumbers = generateLottoNumbers();
        if (numbersGeneratorValidator.valid(lottoNumbers)) {
            for (Integer lottoNumber : lottoNumbers) {
                return new WinningNumbersDto(Set.of(lottoNumber));
            }
        }
        winningNumbersNotFound();
        return new WinningNumbersDto(Set.of(0));
    }

    public LottoNumbersDto selectLottoNumbers(NumbersGenerator numbersGenerator) {
        if (numbersGenerator != null) {
            return numbersGeneratorRepositoryImpl.createUserLottoNumbers(numbersGenerator);
        }
        throw new IllegalArgumentException();
    }

    public LottoNumbersDto findLottoNumbers(UUID uuid) {
        if (uuid == null) {
            log.info("lotto numbers id not found");
        }
        return numbersGeneratorRepositoryImpl.searchUserLottoNumbersByUUID(uuid);
    }
}
