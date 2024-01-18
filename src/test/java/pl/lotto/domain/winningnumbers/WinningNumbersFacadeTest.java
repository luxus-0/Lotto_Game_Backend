package pl.lotto.domain.winningnumbers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.winningnumbers.dto.WinningTicketResponseDto;
import pl.lotto.domain.winningnumbers.exceptions.OutOfRangeNumbersException;
import pl.lotto.domain.winningnumbers.exceptions.WinningNumbersNotFoundException;
import pl.lotto.domain.randomnumbersgenerator.RandomNumbersGenerator;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.lotto.domain.winningnumbers.WinningNumbersFacadeTestImpl.isNumbersInRange;

@RequiredArgsConstructor
@Log4j2
class WinningNumbersFacadeTest {
    DrawDateFacade drawDateFacade = mock(DrawDateFacade.class);
    NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);
   private final WinningNumbersRepository winningNumbersRepository = new WinningNumbersRepositoryTestImpl();

    @Test
    public void should_return_set_of_required_size() {
        //given
        RandomNumbersGenerator randomNumbersGenerator = new RandomNumbersGeneratorTestImpl();
        WinningNumbersFacade winningNumbersFacade = new WinningNumbersFacadeConfiguration()
                .winningNumbersFacade(randomNumbersGenerator, winningNumbersRepository, numberReceiverFacade);


        when(numberReceiverFacade.retrieveInputNumbersByDrawDate(any())).thenReturn(Set.of(10,20,30,40,50,60));

        //when
        WinningTicketResponseDto actualWinningTicket = winningNumbersFacade.generateWinningNumbers();
        //then
        assertThat(actualWinningTicket.winningNumbers().size()).isEqualTo(6);
    }

    @Test
    public void should_return_set_of_required_size_with_required_range() {
        //given
        RandomNumbersGenerator randomNumbersGenerator = new RandomNumbersGeneratorTestImpl();
        WinningNumbersFacade winningNumbersFacade = new WinningNumbersFacadeConfiguration()
                .winningNumbersFacade(randomNumbersGenerator, winningNumbersRepository, numberReceiverFacade);

        when(numberReceiverFacade.retrieveInputNumbersByDrawDate(any())).thenReturn(Set.of(10,20,30,40,50,60));

        //when
        Set<Integer> actualNumbers = winningNumbersFacade.generateWinningNumbers().winningNumbers();
        boolean numbersInRange = isNumbersInRange(actualNumbers);

        //then
       assertTrue(numbersInRange);
    }

    @Test
    public void should_throw_an_exception_when_number_not_in_range() {
        //given
        Set<Integer> numbersOutOfRange = Set.of(10, 20, 30, 40, 50, 100);

        //when
        //then
        assertAll(() -> Assertions.assertThrows(OutOfRangeNumbersException.class,
                () -> {
            RandomNumbersGenerator randomNumbersGenerator = new RandomNumbersGeneratorTestImpl(numbersOutOfRange);
            randomNumbersGenerator.generateRandomNumbers(6, 1, 100);
            },
                "Numbers out of range"));
    }

    @Test
    public void should_return_collection_of_unique_values() {
        //given
        RandomNumbersGenerator randomNumbersGenerator = new RandomNumbersGeneratorTestImpl();
        WinningNumbersFacade winningNumbersFacade = new WinningNumbersFacadeConfiguration()
                .winningNumbersFacade(randomNumbersGenerator, winningNumbersRepository, numberReceiverFacade);

       when(numberReceiverFacade.retrieveInputNumbersByDrawDate(any())).thenReturn(Set.of(10,20,30,40,50,60));

        //when
        WinningTicketResponseDto generateWinningNumbers = winningNumbersFacade.generateWinningNumbers();
        //then
        int sizeNumbers = generateWinningNumbers.winningNumbers().size();
        assertThat(sizeNumbers).isEqualTo(6);
    }

    @Test
    public void should_return_false_when_size_numbers_is_more_than_six() {
        //given
        RandomNumbersGenerator randomNumbersGenerator = new RandomNumbersGeneratorTestImpl();
        WinningNumbersFacade winningNumbersFacade = new WinningNumbersFacadeConfiguration()
                .winningNumbersFacade(randomNumbersGenerator, winningNumbersRepository, numberReceiverFacade);

        when(numberReceiverFacade.retrieveInputNumbersByDrawDate(any())).thenReturn(Set.of(10,20,30,40,50,60));

        //when
        WinningTicketResponseDto actualWinningTicket = winningNumbersFacade.generateWinningNumbers();
        Set<Integer> numbers = actualWinningTicket.winningNumbers();

        //then
        assertFalse(numbers.size() > 6);
    }

    @Test
    public void should_throw_an_exception_when_failed_numbers_by_given_date() {
        //given
        RandomNumbersGenerator randomNumbersGenerator = new RandomNumbersGeneratorTestImpl();
        WinningNumbersFacade winningNumbersFacade = new WinningNumbersFacadeConfiguration()
                .winningNumbersFacade(randomNumbersGenerator, winningNumbersRepository, numberReceiverFacade);

        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 20, 12, 0, 0);

        //when
        //then
        assertThrows(WinningNumbersNotFoundException.class,
                () -> winningNumbersFacade.retrieveWinningNumbersByDate(drawDate));
    }

    @Test
    public void should_return_winning_numbers_by_given_date() {
        //given
        RandomNumbersGenerator randomNumbersGenerator = new RandomNumbersGeneratorTestImpl();
        WinningNumbersFacade winningNumbersFacade = new WinningNumbersFacadeConfiguration()
                .winningNumbersFacade(randomNumbersGenerator, winningNumbersRepository, numberReceiverFacade);

        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 20, 12, 0, 0);

        winningNumbersRepository.save(WinningNumbers.builder()
                        .ticketUUID(UUID.randomUUID().toString())
                        .winningNumbers(Set.of(1,2,3,4))
                        .drawDate(drawDate)
                        .build());
        //when
        WinningTicketResponseDto result = winningNumbersFacade.retrieveWinningNumbersByDate(drawDate);

        //then
        assertThat(result).isNotNull();

    }

    @Test
    public void should_throw_an_exception_when_winning_numbers_not_saved_to_database() {
        //given
        RandomNumbersGenerator randomNumbersGenerator = new RandomNumbersGeneratorTestImpl();
        WinningNumbersFacade winningNumbersFacade = new WinningNumbersFacadeConfiguration()
                .winningNumbersFacade(randomNumbersGenerator, winningNumbersRepository, numberReceiverFacade);

        LocalDateTime drawDate = LocalDateTime.of(2023, 12, 23,12,0,0);

        //when
        //then
        assertThrows(WinningNumbersNotFoundException.class, () -> winningNumbersFacade.retrieveWinningNumbersByDate(drawDate));
    }

    @Test
    public void should_return_false_when_expected_draw_date_is_not_equal_actual_draw_date() {
        //given
        LocalDateTime expectedDrawDate = LocalDateTime.of(2022, 12, 17, 12, 0, 0);

        when(drawDateFacade.retrieveNextDrawDate())
                .thenReturn(LocalDateTime.of(2023,12, 11, 12, 0, 0));
        //when
        LocalDateTime actualDateDraw = drawDateFacade.retrieveNextDrawDate();
        //then
        assertFalse(actualDateDraw.isEqual(expectedDrawDate));
    }

    @Test
    public void should_return_true_when_numbers_are_generated_by_given_date() {
        //given
        RandomNumbersGenerator randomNumbersGenerator = new RandomNumbersGeneratorTestImpl();
        WinningNumbersFacade winningNumbersFacade = new WinningNumbersFacadeConfiguration()
                .winningNumbersFacade(randomNumbersGenerator, winningNumbersRepository, numberReceiverFacade);

        //when
        boolean areWinningNumbersGeneratedByDate = winningNumbersFacade.areWinningNumbersGeneratedByDate();
        //then
        assertTrue(areWinningNumbersGeneratedByDate);
    }
}