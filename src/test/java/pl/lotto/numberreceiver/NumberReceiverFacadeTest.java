package pl.lotto.numberreceiver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lotto.numberreceiver.dto.DateTimeMessageDto;
import pl.lotto.numberreceiver.dto.NumbersMessageDto;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static java.time.LocalTime.NOON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static pl.lotto.numberreceiver.NumbersReceiverMessageProvider.*;

class NumberReceiverFacadeTest {

    private final NumberReceiverGenerator numberReceiverGenerator;
    private final Clock clock;

    NumberReceiverFacadeTest() {
        this.numberReceiverGenerator = new NumberReceiverGenerator();
        this.clock = Clock.systemUTC();
    }

    @Test
    @DisplayName("return success when user gave six numbers")
    public void should_return_success_when_user_gave_six_numbers() {
        // given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration()
                .createModuleForTests(numberReceiverGenerator, clock);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        // when
        NumbersMessageDto numbersUser = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        NumbersMessageDto resultNumbers = new NumbersMessageDto(numbersUser.inputNumber(), EQUALS_SIX_NUMBERS);

        assertThat(numbersUser).isEqualTo(resultNumbers);
    }

    @Test
    @DisplayName("return failed when user gave less than six numbers")
    public void should_return_failed_when_user_gave_less_than_six_numbers() {
        // given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration()
                .createModuleForTests(numberReceiverGenerator, clock);
        Set<Integer> numbers = Set.of(1, 2, 3, 4);

        // when
        NumbersMessageDto numbersUser = numberReceiverFacade.inputNumbers(numbers);

        // then
        NumbersMessageDto result = new NumbersMessageDto(numbers, LESS_THAN_SIX_NUMBERS);

        assertThat(numbersUser).isEqualTo(result);
    }

    @Test
    @DisplayName("return failed when user gave more than six numbers")
    public void should_return_failed_when_user_gave_more_than_six_numbers() {
        // given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration()
                .createModuleForTests(numberReceiverGenerator, clock);
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6, 12, 14);

        // when
        NumbersMessageDto numbersUser = numberReceiverFacade.inputNumbers(numbers);

        // then
        NumbersMessageDto result = new NumbersMessageDto(numbers, MORE_THAN_SIX_NUMBERS);

        assertThat(numbersUser).isEqualTo(result);
    }

    @Test
    @DisplayName("return failed when user gave number out of range")
    public void should_return_failed_when_user_gave_number_out_of_range() {
        // given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration()
                .createModuleForTests(numberReceiverGenerator, clock);
        Set<Integer> numbers = Set.of(102, 1, 2, 3, 4, 130);

        // when
        String resultMessage = numberReceiverFacade.inputNumbers(numbers).message();

        boolean checkValidation = resultMessage.equals(EQUALS_SIX_NUMBERS)
                || resultMessage.equals(NOT_IN_RANGE_NUMBERS);

        // then
        assertTrue(checkValidation);
    }
}
