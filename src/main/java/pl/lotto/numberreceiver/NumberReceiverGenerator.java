package pl.lotto.numberreceiver;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;
import static pl.lotto.numberreceiver.NumbersReceiverMessageProvider.*;

class NumberReceiverGenerator {

    public NumberReceiver generateTicket(UUID uuid, Set<Integer> numbersUser, LocalDateTime drawDate) {
        return NumberReceiver.builder()
                .uuid(uuid)
                .numbersFromUser(numbersUser)
                .drawDate(drawDate)
                .build();
    }

    public Set<Integer> generateLottoNumbers() {
        return IntStream.rangeClosed(SIZE_MIN, SIZE_MAX)
                .map(mapper -> readNumbers())
                .boxed()
                .collect(toSet());
    }

    private int readNumbers() {
        return new Random().nextInt(RANGE_FROM_NUMBER, RANGE_TO_NUMBER);
    }
}
