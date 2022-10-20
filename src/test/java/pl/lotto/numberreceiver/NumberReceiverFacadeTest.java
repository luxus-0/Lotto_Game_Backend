package pl.lotto.numberreceiver;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class NumberReceiverFacadeTest {

    @Test
    public void should_return_success_when_user_gave_six_numbers() {
        // given
        NumberValidator numberValidator = new NumberValidator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade(numberValidator);
        List<Integer> numbersFromUser = List.of(1, 2, 3, 4, 5, 6);
        // when
        String result = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(result).isEqualTo("success");
    }

    @Test
    public void should_return_failed_when_user_gave_less_than_six_numbers() {
        // given
        NumberValidator numberValidator = new NumberValidator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade(numberValidator);
        List<Integer> numbersFromUser = List.of(1, 2, 3, 4);
        // when
        String result = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(result).isEqualTo("failed");
    }

    @Test
    public void should_return_failed_when_user_gave_more_than_six_numbers() {
    }

    @Test
    public void should_return_failed_when_user_gave_duplicated_number() {
    }

    @Test
    public void should_return_failed_when_user_gave_number_out_of_range() {
    }

}
