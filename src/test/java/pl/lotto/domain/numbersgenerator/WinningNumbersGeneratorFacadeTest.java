package pl.lotto.domain.numbersgenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
@Log4j2
class WinningNumbersGeneratorFacadeTest {
    DrawDateFacade drawDateFacade = mock(DrawDateFacade.class);

    @Test
    public void should_return_set_of_required_size() {
        //given
        RandomNumbersGenerable generator = new WinningNumberGeneratorTestImpl();
        WinningNumbersRepository winningNumbersRepository = new WinningNumbersRepositoryTestImpl();
        WinningNumbersGeneratorFacade winningNumbersGeneratorFacade = new WinningNumbersGeneratorFacadeConfiguration()
                .createModuleForTest(drawDateFacade, generator, winningNumbersRepository);

        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        //when
        WinningNumbersDto generateNumbers = winningNumbersGeneratorFacade.generateWinningNumbers();
        //then
        assertThat(generateNumbers.winningNumbers().size()).isEqualTo(6);
    }

    @Test
    public void should_return_set_of_required_size_within_required_range() {
        //given
        RandomNumbersGenerable generator = new WinningNumberGeneratorTestImpl();
        WinningNumbersRepository winningNumbersRepository = new WinningNumbersRepositoryTestImpl();
        WinningNumbersGeneratorFacade winningNumbersGeneratorFacade = new WinningNumbersGeneratorFacadeConfiguration()
                .createModuleForTest(drawDateFacade, generator, winningNumbersRepository);

        int upperBand = 99;
        int lowerBand = 1;

        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        //when
        Set<Integer> winningNumbers = winningNumbersGeneratorFacade.generateWinningNumbers().winningNumbers();
        //then
        boolean numbersInRange = winningNumbers.stream()
                .anyMatch(numbers -> numbers >= lowerBand && numbers <= upperBand);

        assertThat(numbersInRange).isTrue();
    }

    @Test
    public void should_throw_an_exception_when_number_not_in_range() {
        //given
        RandomNumbersGenerable generator = new WinningNumberGeneratorTestImpl();
        WinningNumbersRepository winningNumbersRepository = new WinningNumbersRepositoryTestImpl();
        WinningNumbersGeneratorFacade winningNumbersGeneratorFacade = new WinningNumbersGeneratorFacadeConfiguration()
                .createModuleForTest(drawDateFacade, generator, winningNumbersRepository);

        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        //when
        Set<Integer> winningNumbers = winningNumbersGeneratorFacade.generateWinningNumbers().winningNumbers();
        //then
        assertThrows(NoSuchElementException.class,
                () ->
                        winningNumbers.stream()
                                .filter(number -> number > 99 || number < 1)
                                .findAny()
                                .orElseThrow());
    }

    @Test
    public void should_return_collection_of_unique_values() {
        //given
        RandomNumbersGenerable generator = new WinningNumberGeneratorTestImpl();
        WinningNumbersRepository winningNumbersRepository = new WinningNumbersRepositoryTestImpl();
        WinningNumbersGeneratorFacade winningNumbersGeneratorFacade = new WinningNumbersGeneratorFacadeConfiguration()
                .createModuleForTest(drawDateFacade, generator, winningNumbersRepository);

        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        //when
        WinningNumbersDto generateWinningNumbers = winningNumbersGeneratorFacade.generateWinningNumbers();
        //then
        int sizeWinningNumbers = generateWinningNumbers.winningNumbers().size();
        assertThat(sizeWinningNumbers).isEqualTo(6);
    }

    @Test
    public void should_return_false_when_size_numbers_is_more_than_six() {
        //given
        RandomNumbersGenerable generator = new WinningNumberGeneratorTestImpl();
        WinningNumbersRepository winningNumbersRepository = new WinningNumbersRepositoryTestImpl();
        WinningNumbersGeneratorFacade winningNumbersGeneratorFacade = new WinningNumbersGeneratorFacadeConfiguration()
                .createModuleForTest(drawDateFacade, generator, winningNumbersRepository);

        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        //when
        WinningNumbersDto generateWinningNumbers = winningNumbersGeneratorFacade.generateWinningNumbers();
        //then
        int sizeWinningNumbers = generateWinningNumbers.winningNumbers().size();
        assertFalse(sizeWinningNumbers > 6);
    }

    @Test
    public void should_throw_an_exception_when_failed_numbers_by_given_date() {
        //given
        RandomNumbersGenerable generator = new WinningNumberGeneratorTestImpl();
        WinningNumbersRepository winningNumbersRepository = new WinningNumbersRepositoryTestImpl();
        WinningNumbersGeneratorFacade winningNumbersGeneratorFacade = new WinningNumbersGeneratorFacadeConfiguration()
                .createModuleForTest(drawDateFacade, generator, winningNumbersRepository);

        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 17, 12, 0, 0);
        //when
        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        //then

        assertThrows(WinningNumbersNotFoundException.class,
                () -> winningNumbersGeneratorFacade.retrieveWinningNumbersByDate(drawDate));
    }

    @Test
    public void should_throw_an_exception_when_failed_date_by_given_incorrect_date() {
        //given
        RandomNumbersGenerable generator = new WinningNumberGeneratorTestImpl();
        WinningNumbersRepository winningNumbersRepository = new WinningNumbersRepositoryTestImpl();
        WinningNumbersGeneratorFacade winningNumbersGeneratorFacade = new WinningNumbersGeneratorFacadeConfiguration()
                .createModuleForTest(drawDateFacade, generator, winningNumbersRepository);

        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 17, 12, 0, 0);
        //when
        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        //then
        assertThrows(RuntimeException.class, () -> winningNumbersGeneratorFacade.retrieveWinningNumbersByDate(drawDate));
    }

    @Test
    public void should_return_false_when_draw_date_is_incorrect_by_given_date() {
        //given
        WinningNumbersRepository winningNumbersRepository = new WinningNumbersRepositoryTestImpl();

        LocalDateTime expectedDrawDate = LocalDateTime.of(2022, 12, 17, 12, 0, 0);

        WinningNumbers winningNumbers = WinningNumbers.builder()
                .hash("123")
                .drawDate(expectedDrawDate)
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .build();
        winningNumbersRepository.save(winningNumbers);
        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(expectedDrawDate);
        //when
        LocalDateTime actualDateDraw = drawDateFacade.retrieveNextDrawDate();
        //then
        assertThat(actualDateDraw).isEqualTo(expectedDrawDate);
    }

    @Test
    public void should_return_true_when_numbers_are_generated_by_given_date() {
        //given
        RandomNumbersGenerable generator = new WinningNumberGeneratorTestImpl();
        WinningNumbersRepository winningNumbersRepository = new WinningNumbersRepositoryTestImpl();
        WinningNumbersGeneratorFacade winningNumbersGeneratorFacade = new WinningNumbersGeneratorFacadeConfiguration()
                .createModuleForTest(drawDateFacade, generator, winningNumbersRepository);

        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 17, 12, 0, 0);
        Set<Integer> generatedWinningNumbers = Set.of(1, 2, 3, 4, 5, 6);

        WinningNumbers winningNumbers = WinningNumbers.builder()
                .hash("123")
                .drawDate(drawDate)
                .winningNumbers(generatedWinningNumbers)
                .build();

        winningNumbersRepository.save(winningNumbers);
        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        //when
        boolean areWinningNumbersGeneratedByDate = winningNumbersGeneratorFacade.areWinningNumbersGeneratedByDate();
        //then
        assertTrue(areWinningNumbersGeneratedByDate);
    }
}