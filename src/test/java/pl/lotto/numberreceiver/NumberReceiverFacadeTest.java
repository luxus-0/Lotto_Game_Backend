package pl.lotto.numberreceiver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class NumberReceiverFacadeTest {

    NumberReceiverRepository numberReceiverRepository = new InMemoryNumberReceiverRepository();
    NumbersReceiverValidator validator = new NumbersReceiverValidator();

    @Test
    @DisplayName("return success when user gave six numbers")
    public void should_return_success_when_user_gave_six_numbers() {
        // given
        Clock clock = Clock.systemUTC();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createModuleForTests(clock, numberReceiverRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        // when
        NumberReceiver numberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        boolean checkSixNumbers = validator.isEqualsSixNumbers(numberReceiver.numbersFromUser());
        // then
        NumberReceiver resultNumbers = new NumberReceiver(numberReceiver.uuid(), numberReceiver.numbersFromUser(), numberReceiver.dateTimeDraw());

        assertThat(numberReceiver).isEqualTo(resultNumbers);
        assertThat(checkSixNumbers).isTrue();
    }

    @Test
    @DisplayName("return failed when user gave less than six numbers")
    public void should_return_failed_when_user_gave_less_than_six_numbers() {
        // given
        Clock clock = Clock.systemUTC();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createModuleForTests(clock, numberReceiverRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4);
        // when
        NumberReceiver numberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        boolean checkLessThanSixNumbers = validator.isLessThanSixNumbers(numberReceiver.numbersFromUser());
        // then
        NumberReceiver resultNumbers = new NumberReceiver(numberReceiver.uuid(), numberReceiver.numbersFromUser(), numberReceiver.dateTimeDraw());

        assertThat(numberReceiver).isEqualTo(resultNumbers);
        assertTrue(checkLessThanSixNumbers);
    }

    @Test
    @DisplayName("return failed when user gave more than six numbers")
    public void should_return_failed_when_user_gave_more_than_six_numbers() {
        // given
        Clock clock = Clock.systemUTC();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createModuleForTests(clock, numberReceiverRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7, 8);
        // when
        NumberReceiver numberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        boolean checkMoreThanSixNumbers = validator.isMoreThanSixNumbers(numberReceiver.numbersFromUser());
        // then
        NumberReceiver resultNumbers = new NumberReceiver(numberReceiver.uuid(), numberReceiver.numbersFromUser(), numberReceiver.dateTimeDraw());

        assertThat(numberReceiver).isEqualTo(resultNumbers);
        assertTrue(checkMoreThanSixNumbers);
    }

    @Test
    @DisplayName("return failed when user gave number out of range")
    public void should_return_failed_when_user_gave_number_out_of_range() {
        // given
        Clock clock = Clock.systemUTC();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createModuleForTests(clock, numberReceiverRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 100, 4, 5, 135,900);
        // when
        NumberReceiver numberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        boolean checkNotInRangeNumbers = validator.isNotInRangeNumbers(numberReceiver.numbersFromUser());
        // then
        NumberReceiver resultNumbers = new NumberReceiver(numberReceiver.uuid(), numberReceiver.numbersFromUser(), numberReceiver.dateTimeDraw());

        assertThat(numberReceiver).isEqualTo(resultNumbers);
        assertTrue(checkNotInRangeNumbers);
    }

    @Test
    @DisplayName("return failed when user gave empty numbers")
    public void should_return_failed_when_user_gave_empty_numbers() {
        // given
        Clock clock = Clock.systemUTC();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createModuleForTests(clock, numberReceiverRepository);
        Set<Integer> numbersFromUser = Set.of();
        // when
        NumberReceiver numberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        boolean checkEmptyNumbers = validator.isEmptyNumbers(numberReceiver.numbersFromUser());
        // then
        NumberReceiver resultNumbers = new NumberReceiver(numberReceiver.uuid(), numberReceiver.numbersFromUser(), numberReceiver.dateTimeDraw());

        assertThat(numberReceiver).isEqualTo(resultNumbers);
        assertTrue(checkEmptyNumbers);
    }

    @Test
    @DisplayName("return failed when user gave six minus numbers")
    public void should_return_failed_when_user_gave_six_minus_numbers() {
        // given
        Clock clock = Clock.systemUTC();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createModuleForTests(clock, numberReceiverRepository);
        Set<Integer> numbersFromUser = Set.of(-20,-34, 3, -13, 5, -44);
        // when
        NumberReceiver numberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        boolean checkPositiveNumbers = validator.isPositiveNumbers(numberReceiver.numbersFromUser());
        boolean checkSixNumbers = validator.isEqualsSixNumbers(numberReceiver.numbersFromUser());
        // then
        NumberReceiver resultNumbers = new NumberReceiver(numberReceiver.uuid(), numberReceiver.numbersFromUser(), numberReceiver.dateTimeDraw());

        assertThat(numberReceiver).isEqualTo(resultNumbers);
        assertThat(checkSixNumbers).isTrue();
        assertFalse(checkPositiveNumbers);
    }

    @Test
    @DisplayName("return success when user gave six numbers and draw date time draw")
    public void should_return_success_when_user_gave_six_numbers_and_date_time_draw() {
        // given
        Clock clock = Clock.systemUTC().withZone(ZoneId.of("Europe/Warsaw"));
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacadeConfiguration().createModuleForTests(clock, numberReceiverRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        LocalDateTime dateTime = LocalDateTime.of(2022, Month.NOVEMBER, 19, 12,0);
        // when
        NumberReceiver numberReceiver = numberReceiverFacade.inputNumbers(numbersFromUser);
        DateTimeDraw dateTimeDraw = numberReceiverFacade.inputDateTimeDraw(numbersFromUser, dateTime);
        boolean checkSixNumbers = validator.isEqualsSixNumbers(numberReceiver.numbersFromUser());
        // then
        NumberReceiver resultNumbers = new NumberReceiver(numberReceiver.uuid(), numberReceiver.numbersFromUser(), numberReceiver.dateTimeDraw());
        DateTimeDraw resultDateTime = new DateTimeDraw(dateTimeDraw.dateTime(), dateTimeDraw.message());

        assertThat(numberReceiver).isEqualTo(resultNumbers);
        assertThat(checkSixNumbers).isTrue();
        assertEquals(dateTimeDraw, resultDateTime);
    }
}
