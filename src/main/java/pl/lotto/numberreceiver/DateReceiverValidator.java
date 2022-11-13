package pl.lotto.numberreceiver;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

class DateReceiverValidator {
    NumbersReceiverValidator numbersValidator = new NumbersReceiverValidator();
    public boolean validate(Set<Integer> numbersUser, LocalDateTime dateTime){
        if(numbersValidator.isEqualsSixNumbers(numbersUser) && isDateTimeDraw(dateTime)){
            return true;
        }
        return false;
    }

    private boolean isDateTimeDraw(LocalDateTime dateTime) {
        return Optional.of(dateTime).stream().findAny().isPresent();
    }
}
