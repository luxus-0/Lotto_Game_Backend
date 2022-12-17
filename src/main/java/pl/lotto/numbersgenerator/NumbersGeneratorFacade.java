package pl.lotto.numbersgenerator;

import org.springframework.stereotype.Component;
import pl.lotto.numbersgenerator.dto.LottoNumbersDto;
import pl.lotto.numbersgenerator.dto.WinningNumbersDto;

import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static pl.lotto.numbersgenerator.WinningNumbersMessageProvider.winningNumbersNotFound;

@Component
public class NumbersGeneratorFacade {

    private static final Integer MIN_NUMBER = 1;
    private static final Integer MAX_NUMBER = 99;
    private final NumbersGeneratorRepository numbersGeneratorRepository;
    private final NumbersGeneratorValidator numbersGeneratorValidator;

    public NumbersGeneratorFacade(NumbersGeneratorRepository numbersGeneratorRepository, NumbersGeneratorValidator numbersGeneratorValidator) {
        this.numbersGeneratorRepository = numbersGeneratorRepository;
        this.numbersGeneratorValidator = numbersGeneratorValidator;
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

    public LottoNumbersDto findNumbersGenerator(NumbersGenerator numbersGenerator) {
        return numbersGeneratorRepository
                .findById(numbersGenerator.uuid())
                .map(toDto -> new LottoNumbersDto(numbersGenerator.uuid(), numbersGenerator.lottoNumbers()))
                .orElseThrow();
    }
}
