package pl.lotto.numberreceiver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lotto.numberreceiver.dto.NumbersResultMessageDto;
import pl.lotto.numberreceiver.enums.ValidateMessage;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static pl.lotto.numberreceiver.NumberReceiverMessageProvider.FAILED_MESSAGE;
import static pl.lotto.numberreceiver.enums.ValidateMessage.CORRECT_SIZE_NUMBERS;
import static pl.lotto.numberreceiver.enums.ValidateMessage.NOT_CORRECT_SIZE_NUMBERS;

public class NumberReceiverFacadeTest {

    @Test
    @DisplayName("Return success when user gave six numbers")
    public void should_return_success_when_user_gave_six_numbers() {
        // given
        NumberReceiverValidator numberValidator = new NumberReceiverValidator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade(numberValidator);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        // when
        NumbersResultMessageDto readNumbers = numberReceiverFacade.isEqualsSixNumbers(numbersFromUser);
        // then
        NumbersResultMessageDto numbersResult = new NumbersResultMessageDto(numbersFromUser, CORRECT_SIZE_NUMBERS.name());
        assertThat(readNumbers).isEqualTo(numbersResult);
    }

    @Test
    @DisplayName("Return failed when user gave less than six numbers")
    public void should_return_failed_when_user_gave_less_than_six_numbers() {
        // given
        NumberReceiverValidator numberValidator = new NumberReceiverValidator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade(numberValidator);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4);
        // when
        NumbersResultMessageDto readNumbers = numberReceiverFacade.isLessThanSixNumbers(numbersFromUser);
        // then
        NumbersResultMessageDto numbersResult = new NumbersResultMessageDto(numbersFromUser, FAILED_MESSAGE);
        assertThat(readNumbers).isEqualTo(numbersResult);
    }

    @Test
    @DisplayName("return not correct size numbers when user gave more than six numbers")
    public void should_return_not_correct_size_numbers_message_when_user_gave_more_than_six_numbers() {
        // given
        NumberReceiverValidator numberValidator = new NumberReceiverValidator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade(numberValidator);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 12, 14);
        // when
        NumbersResultMessageDto readNumbers = numberReceiverFacade.isMoreThanSixNumbers(numbersFromUser);
        // then
        NumbersResultMessageDto numbersResult = new NumbersResultMessageDto(numbersFromUser, NOT_CORRECT_SIZE_NUMBERS.name());
        assertThat(readNumbers).isEqualTo(numbersResult);
    }

    @Test
    @DisplayName("Return duplicate numbers message when user gave duplicated number")
    public void should_return_duplicate_numbers_message_when_user_gave_duplicated_number() {
        // given
        NumberReceiverValidator numberValidator = new NumberReceiverValidator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade(numberValidator);
        List<Integer> numbers = List.of(1, 2, 2, 4, 5, 6, 6, 14);
        // when
        ValidateMessage readNumbers = numberReceiverFacade.isDuplicateNumbers(numbers);
        // then
        ValidateMessage messageResult = ValidateMessage.DUPLICATE_NUMBERS;
        assertThat(readNumbers).isEqualTo(messageResult);
    }

    @Test
    @DisplayName("Return counting numbers when user gave duplicated numbers")
    public void should_return_counting_numbers_when_user_gave_duplicated_number() {
        // given
        NumbersDuplicationCounter numberReceiverCounter = new NumbersDuplicationCounter();
        List<Integer> numbers = List.of(1, 2, 2, 4, 5, 6, 6, 14);
        // when
        ValidateMessage readNumbers = numberReceiverCounter.printInfo(numbers);
        // then
        ValidateMessage messageResult = ValidateMessage.DUPLICATE_NUMBERS;
        assertThat(readNumbers).isEqualTo(messageResult);
    }

    @Test
    @DisplayName("Return not in range numbers when user gave number out of range")
    public void should_return_not_in_range_numbers_message_when_user_gave_number_out_of_range() {
        // given
        NumberReceiverValidator numberValidator = new NumberReceiverValidator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade(numberValidator);
        Set<Integer> numbers = Set.of(100, 1, 2, 3, 4, 200);
        // when
        ValidateMessage findNumbers = numberReceiverFacade.isNumbersNotInRange(numbers);
        // then
        assertNotEquals(ValidateMessage.NOT_IN_RANGE_NUMBERS.name(), findNumbers.name());
    }

    @Test
    @DisplayName("Return in range numbers when user gave number in range")
    public void should_return_in_range_numbers_message_when_user_gave_number_in_range() {
        // given
        NumberReceiverValidator numberValidator = new NumberReceiverValidator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade(numberValidator);
        Set<Integer> numbers = Set.of(90, 1, 2, 3, 4, 98);
        // when
        ValidateMessage findNumbers = numberReceiverFacade.isNumbersNotInRange(numbers);
        // then
        assertEquals("IN_RANGE_NUMBERS", findNumbers.name());
    }
}
