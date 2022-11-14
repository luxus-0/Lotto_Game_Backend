package pl.lotto.numberreceiver;

import java.time.*;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.LocalTime.NOON;
import static java.time.MonthDay.now;
import static pl.lotto.numberreceiver.DateTimeDrawMessageProvider.*;

public class NumberReceiverFacade {

    private final Clock clock;
    private final NumbersReceiverValidator numberValidator;
    private final DateReceiverValidator dateValidator;
    private final NumberReceiverRepository numberReceiverRepository;

    public NumberReceiverFacade(Clock clock, NumbersReceiverValidator numberValidator, DateReceiverValidator dateValidator, NumberReceiverRepository numberReceiverRepository) {
        this.clock = clock;
        this.numberValidator = numberValidator;
        this.dateValidator = dateValidator;
        this.numberReceiverRepository = numberReceiverRepository;
    }

    public NumberReceiver inputNumbers(Set<Integer> numbersFromUser) {
        boolean validate = numberValidator.validate(numbersFromUser);
        UUID uuid = UUID.randomUUID();
        DateTimeDraw dateTimeDraw = inputDateTimeDraw(numbersFromUser, LocalDateTime.now(clock));
        NumberReceiver numberReceiver = new NumberReceiver(uuid, numbersFromUser, dateTimeDraw);
        if (validate) {
            return numberReceiverRepository.save(numberReceiver);
        }
        return Optional.of(numberReceiver).get();
    }

    public DateTimeDraw inputDateTimeDraw(Set<Integer> numbersFromUser, LocalDateTime dateTimeDraw) {
        if (dateValidator.validate(numbersFromUser, dateTimeDraw)) {
            int actualYear = Year.now(clock).getValue();
            int actualMonth = Month.from(now(clock)).getValue();
            int actualDay = MonthDay.from(now(clock)).withDayOfMonth(SATURDAY.getValue()).getDayOfMonth();
            LocalDate actualDate = LocalDate.of(actualYear, actualMonth, actualDay);
            LocalDateTime todayTwelveAm = LocalDateTime.of(actualDate, NOON);
            return Stream.of(dateTimeDraw)
                    .filter(checkDateTime -> dateTimeDraw.equals(todayTwelveAm))
                    .map(dateTime -> new DateTimeDraw(todayTwelveAm, SUCCESS_DRAW_DATE_TIME))
                    .findAny()
                    .orElse(new DateTimeDraw(null, FAILED_DRAW__DATE_TIME));
        }
        throw new IllegalArgumentException();
    }
}