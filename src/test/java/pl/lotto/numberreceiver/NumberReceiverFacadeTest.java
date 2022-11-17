package pl.lotto.numberreceiver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lotto.numberreceiver.dto.AllUsersNumbersDto;
import pl.lotto.numberreceiver.dto.NumberReceiverDto;
import pl.lotto.numberreceiver.dto.UserNumbersDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.time.Month.DECEMBER;
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class NumberReceiverFacadeTest {

    NumberReceiverRepository numberReceiverRepository = new InMemoryNumberReceiverRepository();

    @Test
    @DisplayName("return success when user gave six numbers")
    public void should_return_success_when_user_gave_six_numbers() {
        // given
        Clock clock = Clock.systemUTC();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createModuleForTests(clock, numberReceiverRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        // when
        NumberReceiverDto numberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(numberReceiver.numbersFromUser()).isEqualTo(numbersFromUser);
    }

    @Test
    @DisplayName("return failed when user gave less than six numbers")
    public void should_return_failed_when_user_gave_less_than_six_numbers() {
        // given
        Clock clock = Clock.systemUTC();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createModuleForTests(clock, numberReceiverRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4);
        // when
        NumberReceiverDto numberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(numberReceiver.numbersFromUser()).isEqualTo(numbersFromUser);
    }

    @Test
    @DisplayName("return failed when user gave more than six numbers")
    public void should_return_failed_when_user_gave_more_than_six_numbers() {
        // given
        Clock clock = Clock.systemUTC();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createModuleForTests(clock, numberReceiverRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7, 8);
        // when
        NumberReceiverDto numberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(numberReceiver.numbersFromUser()).isEqualTo(numbersFromUser);
    }

    @Test
    @DisplayName("return failed when user gave number out of range")
    public void should_return_failed_when_user_gave_number_out_of_range() {
        // given
        Clock clock = Clock.systemUTC();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createModuleForTests(clock, numberReceiverRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 100, 4, 5, 135,900);
        // when
        NumberReceiverDto numberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(numberReceiver.numbersFromUser()).isEqualTo(numbersFromUser);
    }

    @Test
    @DisplayName("return failed when user gave empty numbers")
    public void should_return_failed_when_user_gave_empty_numbers() {
        // given
        Clock clock = Clock.systemUTC();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createModuleForTests(clock, numberReceiverRepository);
        Set<Integer> numbersFromUser = Set.of();
        // when
        NumberReceiverDto numberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(numberReceiver.numbersFromUser()).isEqualTo(numbersFromUser);
    }

    @Test
    @DisplayName("return failed when user gave six minus numbers")
    public void should_return_failed_when_user_gave_six_minus_numbers() {
        // given
        Clock clock = Clock.systemUTC();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createModuleForTests(clock, numberReceiverRepository);
        Set<Integer> numbersFromUser = Set.of(-20,-34, 3, -13, 5, -44);
        // when
        NumberReceiverDto numberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(numberReceiver.numbersFromUser()).isEqualTo(numbersFromUser);
    }

    @Test
    @DisplayName("return success when user gave correct draw date time")
    public void should_return_success_when_user_gave_correct_date_time_draw() {
        // given
        LocalDateTime datetimeDraw = LocalDateTime.of(2022, DECEMBER, 3, 12, 0);
        Clock clock = Clock.fixed(datetimeDraw.toInstant(UTC), ZoneId.systemDefault());
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createModuleForTests(clock, numberReceiverRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        // when
        NumberReceiverDto numberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        LocalDateTime resultDateTimeDraw = LocalDateTime.now(clock)
                .withYear(2022).withMonth(DECEMBER.getValue()).withDayOfMonth(10)
                .withHour(12).withMinute(0);

        assertThat(numberReceiver.dateTimeDraw()).isEqualTo(resultDateTimeDraw);
    }

    @Test
    @DisplayName("return success when user gave incorrect draw date time")
    public void should_return_failed_when_user_gave_incorrect_date_time_draw() {
        // given
        LocalDateTime datetimeDraw = LocalDateTime.of(2022, DECEMBER, 3, 12, 0);
        Clock clock = Clock.fixed(datetimeDraw.toInstant(UTC), ZoneId.systemDefault());
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createModuleForTests(clock, numberReceiverRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        // when
        NumberReceiverDto numberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        LocalDateTime resultDateTimeDraw = LocalDateTime.now(clock)
                .withYear(2022).withMonth(12).withDayOfMonth(9)
                .withHour(12).withMinute(0);

        assertNotEquals(numberReceiver.dateTimeDraw(), resultDateTimeDraw);
    }

    @Test
    @DisplayName("return success when user gave correct draw date time user numbers")
    public void should_return_success_when_user_gave_correct_date_time_draw_user_numbers() {
        // given
        LocalDateTime datetimeDraw = LocalDateTime.of(2022, DECEMBER, 3, 12, 0);
        Clock clock = Clock.fixed(datetimeDraw.toInstant(UTC), ZoneId.systemDefault());
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createModuleForTests(clock, numberReceiverRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        // when
        AllUsersNumbersDto allUsersNumbers = numberReceiverFacade.usersNumbers(datetimeDraw);
        // then
        List<UserNumbersDto> usersNumbers = List.of(new UserNumbersDto(UUID.randomUUID(), numbersFromUser, datetimeDraw));
        AllUsersNumbersDto resultAllUsersNumbers = new AllUsersNumbersDto(usersNumbers);

        assertThat(allUsersNumbers).isEqualTo(resultAllUsersNumbers);
    }
}
