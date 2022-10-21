package pl.lotto.numberreceiver;

import org.junit.jupiter.api.Test;
import pl.lotto.numberreceiver.dto.NumbersResultMessageDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.lotto.numberreceiver.NumberReceiverMessageProvider.FAILED_MESSAGE;
import static pl.lotto.numberreceiver.NumberReceiverMessageProvider.SUCCESS_MESSAGE;
import static pl.lotto.numberreceiver.enums.ValidateMessageInfo.CORRECT_SIZE_NUMBERS;
import static pl.lotto.numberreceiver.enums.ValidateMessageInfo.NOT_CORRECT_SIZE_NUMBERS;

public class NumberReceiverFacadeTest {

    @Test
    public void should_return_success_when_user_gave_six_numbers() {
        // given
        NumberValidator numberValidator = new NumberValidator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade(numberValidator);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        // when
        NumbersResultMessageDto readNumbers = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        NumbersResultMessageDto numbersResult = new NumbersResultMessageDto(numbersFromUser, CORRECT_SIZE_NUMBERS.name());
        assertThat(readNumbers).isEqualTo(numbersResult);
    }

    @Test
    public void should_return_failed_when_user_gave_less_than_six_numbers() {
        // given
        NumberValidator numberValidator = new NumberValidator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade(numberValidator);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4);
        // when
        NumbersResultMessageDto readNumbers = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        NumbersResultMessageDto numbersResult = new NumbersResultMessageDto(numbersFromUser, FAILED_MESSAGE);
        assertThat(readNumbers).isEqualTo(numbersResult);
    }

    @Test
    public void should_return_failed_when_user_gave_more_than_six_numbers() {
        // given
        NumberValidator numberValidator = new NumberValidator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade(numberValidator);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 12, 14);
        // when
        NumbersResultMessageDto readNumbers = numberReceiverFacade.isMoreThanSixNumbers(numbersFromUser);
        // then
        NumbersResultMessageDto numbersResult = new NumbersResultMessageDto(numbersFromUser, NOT_CORRECT_SIZE_NUMBERS.name());
        assertThat(readNumbers).isEqualTo(numbersResult);
    }

    @Test
    public void should_return_failed_when_user_gave_duplicated_number() {
    }

    @Test
    public void should_return_failed_when_user_gave_number_out_of_range() {
    }

}
