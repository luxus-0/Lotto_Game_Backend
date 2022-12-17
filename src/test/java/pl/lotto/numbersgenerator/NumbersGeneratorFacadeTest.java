package pl.lotto.numbersgenerator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pl.lotto.datetimegenerator.DateTimeDrawFacade;
import pl.lotto.numbersgenerator.dto.LottoNumbersDto;

import java.time.Clock;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

class NumbersGeneratorFacadeTest {

    private final Clock clock = Clock.systemDefaultZone();
    private final DateTimeDrawFacade dateTimeDrawFacade = new DateTimeDrawFacade(clock);
    private final NumbersGeneratorRepository numbersGeneratorRepository;
    NumbersGeneratorValidator numbersGeneratorValidator = new NumbersGeneratorValidator();

    NumbersGeneratorFacadeTest(NumbersGeneratorRepository numbersGeneratorRepository) {
        this.numbersGeneratorRepository = numbersGeneratorRepository;
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    void shouldReturnFalseWhenUserGaveNotRandomNumbers(int randomNumber){
        //given
        NumbersGeneratorFacade numbersGeneratorFacade = new NumbersGeneratorFacadeConfiguration()
                .createModuleForTests(numbersGeneratorRepository);
        //when
        Set<Integer> lottoNumbers = numbersGeneratorFacade.generateLottoNumbers();
        //then
        assertFalse(lottoNumbers.contains(randomNumber));
    }

    @ParameterizedTest
    @ValueSource(strings = {"234-078", "111-231"})
    void shouldReturnTrueWhenUserSaveRandomNumbersToDatabase(String uuid){
        //given
        NumbersGeneratorFacade numbersGeneratorFacade = new NumbersGeneratorFacadeConfiguration()
                .createModuleForTests(numbersGeneratorRepository);
        NumbersGenerator numbersGenerator = new NumbersGenerator(UUID.randomUUID(), Set.of(1, 2, 3, 4, 5, 6), dateTimeDrawFacade.readNextDrawDate());
        //when
        LottoNumbersDto savedNumberGenerator = numbersGeneratorFacade.createNumbersGenerator(numbersGenerator);
        //then
        assertThat(savedNumberGenerator.uuid().toString()).isEqualTo(uuid);
    }
}