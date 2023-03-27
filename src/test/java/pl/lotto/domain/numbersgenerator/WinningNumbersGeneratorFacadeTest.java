package pl.lotto.domain.numbersgenerator;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.configurationprocessor.json.JSONException;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
class WinningNumbersGeneratorFacadeTest {

    @Mock
    RandomNumberRepository randomNumberRepository;

    WinningNumbersRepository winningNumbersRepository = new WinningNumbersRepositoryTestImpl();
    DrawDateFacade drawDateFacade = mock(DrawDateFacade.class);

    RandomNumberGeneratorFacade randomNumberGeneratorFacade = new RandomNumberGeneratorConfiguration().createModuleForTests(randomNumberRepository);

    WinningNumbersGeneratorFacade winningNumbersGeneratorFacade = new WinningNumbersGeneratorFacadeConfiguration().createModuleForTest(randomNumberGeneratorFacade, drawDateFacade, winningNumbersRepository);

    @Test
    public void should_return_set_of_required_size() throws JSONException {
        //given
        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());

        WinningNumbersGeneratorFacade winningNumbersGeneratorFacade = new WinningNumbersGeneratorFacadeConfiguration().createModuleForTest(randomNumberGeneratorFacade, drawDateFacade, winningNumbersRepository);
        //when
        WinningNumbersDto generateNumbers = winningNumbersGeneratorFacade.generateWinningNumbers();
        //then
        assertThat(generateNumbers.winningNumbers().size()).isEqualTo(6);
    }

}