package pl.lotto.domain.numbersgenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numbersgenerator.dto.RandomNumbersResponseDto;
import pl.lotto.domain.numbersgenerator.dto.WinningTicketResponseDto;
import pl.lotto.domain.numbersgenerator.exceptions.OutOfRangeNumbersException;
import pl.lotto.domain.numbersgenerator.exceptions.WinningNumbersNotFoundException;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
@Log4j2
class WinningNumbersFacadeTest {
    DrawDateFacade drawDateFacade = mock(DrawDateFacade.class);
    WinningNumbersRepository winningNumbersRepository = mock(WinningNumbersRepository.class);
    RandomNumbersGenerator randomNumbersGenerator = mock(RandomNumbersGenerator.class);
    NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);

    @Test
    public void should_return_set_of_required_size() {
        //given
        WinningNumbersFacade winningNumbersFacade = new WinningNumbersFacadeConfiguration()
                .winningNumbersFacade(drawDateFacade, winningNumbersRepository, numberReceiverFacade);

        when(winningNumbersRepository.save(any(WinningNumbers.class)))
                .thenReturn(WinningNumbers.builder()
                        .ticketId("123456")
                        .drawDate(LocalDateTime.now())
                        .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                        .build());
        //when
        WinningTicketResponseDto actualWinningTicket = winningNumbersFacade.generateWinningNumbers();
        //then
        assertThat(actualWinningTicket.winningNumbers().size()).isEqualTo(6);
    }

    @Test
    public void should_return_set_of_required_size_within_required_range() {
        //given
        WinningNumbersFacade winningNumbersFacade = new WinningNumbersFacadeConfiguration()
                .winningNumbersFacade(drawDateFacade, winningNumbersRepository, numberReceiverFacade);

        when(randomNumbersGenerator.generateRandomNumbers(6, 1, 99))
                .thenReturn(RandomNumbersResponseDto.builder()
                        .randomNumbers(Set.of(1, 2, 3, 4, 5, 6))
                        .build());

        when(winningNumbersRepository.save(any(WinningNumbers.class)))
                .thenReturn(WinningNumbers.builder()
                        .ticketId("12345")
                        .winningNumbers(Set.of(2, 3, 4, 11, 14, 90))
                        .drawDate(LocalDateTime.now())
                        .build());
        //when
        //then
        boolean numbersInRange = winningNumbersFacade.generateWinningNumbers().winningNumbers().stream()
                .anyMatch(numbers -> numbers >= 1 && numbers <= 99);

        assertThat(numbersInRange).isTrue();
    }

    @Test
    public void should_throw_an_exception_when_number_not_in_range() {
        //given
        WinningNumbersFacade winningNumbersFacade = new WinningNumbersFacadeConfiguration()
                .winningNumbersFacade(drawDateFacade, winningNumbersRepository, numberReceiverFacade);

        when(winningNumbersRepository.save(any(WinningNumbers.class)))
                .thenReturn(WinningNumbers.builder()
                        .ticketId("123456")
                        .drawDate(LocalDateTime.now())
                        .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                        .build());
        //when
        Set<Integer> winningNumbers = winningNumbersFacade.generateWinningNumbers().winningNumbers();
        //then
        assertThrows(OutOfRangeNumbersException.class,
                () ->
                        winningNumbers.stream()
                                .filter(number -> number < 1 || number > 99)
                                .findAny()
                                .orElseThrow(() -> new OutOfRangeNumbersException("Numbers out of range")));
    }

    @Test
    public void should_return_collection_of_unique_values() {
        //given
        WinningNumbersFacade winningNumbersFacade = new WinningNumbersFacadeConfiguration()
                .winningNumbersFacade(drawDateFacade, winningNumbersRepository, numberReceiverFacade);

        when(winningNumbersRepository.save(any(WinningNumbers.class)))
                .thenReturn(WinningNumbers.builder()
                        .ticketId("123456")
                        .drawDate(LocalDateTime.now())
                        .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                        .build());
        //when
        WinningTicketResponseDto generateWinningNumbers = winningNumbersFacade.generateWinningNumbers();
        //then
        int sizeWinningNumbers = generateWinningNumbers.winningNumbers().size();
        assertThat(sizeWinningNumbers).isEqualTo(6);
    }

    @Test
    public void should_return_false_when_size_numbers_is_more_than_six() {
        //given
        WinningNumbersFacade winningNumbersFacade = new WinningNumbersFacadeConfiguration()
                .winningNumbersFacade(drawDateFacade, winningNumbersRepository, numberReceiverFacade);

        //when
        when(winningNumbersRepository.save(any(WinningNumbers.class)))
                .thenReturn(WinningNumbers.builder()
                        .ticketId("123456")
                        .drawDate(LocalDateTime.now())
                        .winningNumbers(Set.of(1, 2, 3, 4, 5, 6, 7))
                        .build());

        WinningTicketResponseDto actualWinningTicket = winningNumbersFacade.generateWinningNumbers();
        //then
        int actualWinningNumbersSize = actualWinningTicket.winningNumbers().size();
        assertTrue(actualWinningNumbersSize > 6);
    }

    @Test
    public void should_throw_an_exception_when_failed_numbers_by_given_date() {
        //given
        WinningNumbersFacade winningNumbersFacade = new WinningNumbersFacadeConfiguration()
                .winningNumbersFacade(drawDateFacade, winningNumbersRepository, numberReceiverFacade);

        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 17, 12, 0, 0);
        //when
        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        //then

        assertThrows(WinningNumbersNotFoundException.class,
                () -> winningNumbersFacade.retrieveWinningNumbersByDate(drawDate));
    }

    @Test
    public void should_throw_an_exception_when_failed_date_by_given_incorrect_date() {
        //given
        WinningNumbersFacade winningNumbersFacade = new WinningNumbersFacadeConfiguration()
                .winningNumbersFacade(drawDateFacade, winningNumbersRepository, numberReceiverFacade);

        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 17, 12, 0, 0);
        //when
        when(winningNumbersRepository.save(any(WinningNumbers.class)))
                .thenReturn(WinningNumbers.builder()
                        .ticketId("123456")
                        .drawDate(LocalDateTime.now())
                        .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                        .build());
        //then
        assertThrows(WinningNumbersNotFoundException.class, () -> winningNumbersFacade.retrieveWinningNumbersByDate(drawDate));
    }

    @Test
    public void should_return_false_when_draw_date_is_incorrect_by_given_date() {
        //given
        LocalDateTime expectedDrawDate = LocalDateTime.of(2022, 12, 17, 12, 0, 0);

        WinningNumbers winningNumbers = WinningNumbers.builder()
                .ticketId("123")
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
        WinningNumbersFacade winningNumbersFacade = new WinningNumbersFacadeConfiguration()
                .winningNumbersFacade(drawDateFacade, winningNumbersRepository, numberReceiverFacade);

        LocalDateTime drawDate = drawDateFacade.retrieveNextDrawDate();

        when(winningNumbersRepository.save(any(WinningNumbers.class)))
                .thenReturn(WinningNumbers.builder()
                        .ticketId("123")
                        .drawDate(drawDate)
                        .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                        .build());
        when(winningNumbersRepository.existsByDrawDate(drawDate))
                .thenReturn(true);
        //when
        boolean areWinningNumbersGeneratedByDate = winningNumbersFacade.areWinningNumbersGeneratedByDate();
        //then
        assertTrue(areWinningNumbersGeneratedByDate);
    }
}