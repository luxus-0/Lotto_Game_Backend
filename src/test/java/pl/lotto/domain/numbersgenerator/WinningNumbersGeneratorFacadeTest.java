package pl.lotto.domain.numbersgenerator;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numbersgenerator.dto.WinningNumbersDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
class WinningNumbersGeneratorFacadeTest {

    WinningNumbersRepository winningNumbersRepository = new WinningNumbersRepositoryTestImpl();
    DrawDateFacade drawDateFacade = mock(DrawDateFacade.class);
    RandomNumberGeneratorFacade randomNumberGeneratorFacade = new RandomNumberGeneratorConfiguration().createModuleForTests(winningNumbersRepository);
    WinningNumbersGeneratorFacade winningNumbersGeneratorFacade = new WinningNumbersGeneratorFacadeConfiguration().createModuleForTest(randomNumberGeneratorFacade, drawDateFacade, winningNumbersRepository);

    @Test
    public void should_return_set_of_required_size() {
        //given
        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        //when
        WinningNumbersDto generateNumbers = winningNumbersGeneratorFacade.generateWinningNumbers();
        //then
        assertThat(generateNumbers.winningNumbers().size()).isEqualTo(6);
    }

    @Test
    public void should_return_set_of_required_size_within_required_range(){
        //given
        int upperBand = 99;
        int lowerBand = 1;

        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        //when
        WinningNumbersDto result = winningNumbersGeneratorFacade.generateWinningNumbers();
        //then
        Set<Integer> winningNumbers = result.winningNumbers();
        boolean numbersInRange = winningNumbers.stream().anyMatch(numbers -> numbers >= lowerBand && numbers <= upperBand);

        assertThat(numbersInRange).isTrue();
    }

    @Test
    public void should_throw_an_exception_when_number_not_in_range(){
        //given
        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        //when
        Set<Integer> winningNumbers = winningNumbersGeneratorFacade.generateWinningNumbers().winningNumbers();
        //then
        assertThrows(Exception.class,
                () ->
                        winningNumbers.stream()
                                .filter(number -> number > 99 || number < 1)
                                .findAny()
                                .orElseThrow(NoSuchElementException::new));
    }

    @Test
    public void should_return_collection_of_unique_values(){
        //given
        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        //when
        WinningNumbersDto generateWinningNumbers = winningNumbersGeneratorFacade.generateWinningNumbers();
        //then
        int sizeWinningNumbers = generateWinningNumbers.winningNumbers().size();
        assertThat(sizeWinningNumbers).isEqualTo(6);
    }
    @Test
    public void should_return_false_when_size_numbers_is_less_than_six() {
        //given
        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        //when
        WinningNumbersDto generateWinningNumbers = winningNumbersGeneratorFacade.generateWinningNumbers();
        //then
        int sizeWinningNumbers = generateWinningNumbers.winningNumbers().size();
        assertFalse(sizeWinningNumbers < 6);
    }

    @Test
    public void should_return_false_when_size_numbers_is_more_than_six() {
        //given
        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        //when
        WinningNumbersDto generateWinningNumbers = winningNumbersGeneratorFacade.generateWinningNumbers();
        //then
        int sizeWinningNumbers = generateWinningNumbers.winningNumbers().size();
        assertFalse(sizeWinningNumbers > 6);
    }

    @Test
    public void should_return_winning_numbers_when_given_ticket() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 17, 12, 0, 0);
        Set<Integer> generatedWinningNumbers = Set.of(1, 2, 3, 4, 5, 6);
        String hash = UUID.randomUUID().toString();
        WinningNumbers winningNumbers = WinningNumbers.builder()
                .hash(hash)
                .drawDate(drawDate)
                .winningNumbers(generatedWinningNumbers)
                .build();

        winningNumbersRepository.create(winningNumbers);
        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        //when
        WinningNumbersDto winningNumbersDto = winningNumbersGeneratorFacade.retrieveWinningNumbersByDate(drawDate);
        //then
        WinningNumbersDto expectedWinningNumbersDto = WinningNumbersDto.builder()
                .drawDate(drawDate)
                .winningNumbers(generatedWinningNumbers)
                .build();
        assertThat(expectedWinningNumbersDto).isEqualTo(winningNumbersDto);
    }
}