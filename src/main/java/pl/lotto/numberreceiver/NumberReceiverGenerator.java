package pl.lotto.numberreceiver;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static pl.lotto.numberreceiver.NumbersReceiverMessageProvider.*;

class NumberReceiverGenerator {

    private static int readNumbers() {
        return new Random().nextInt(RANGE_FROM_NUMBER, RANGE_TO_NUMBER);
    }

    public NumberReceiver generateUserTicket(Set<Integer> numbersUser, LocalDateTime drawDate) {
        String uuid = UUID.randomUUID().toString();
        return NumberReceiver.builder()
                .uuid(uuid)
                .numbersFromUser(numbersUser)
                .drawDate(drawDate)
                .build();
    }

    public Set<Integer> generateLottoTicket() {
        return IntStream.rangeClosed(SIZE_MIN, SIZE_MAX)
                .map(mapper -> readNumbers())
                .boxed()
                .collect(Collectors.toSet());
    }
}
