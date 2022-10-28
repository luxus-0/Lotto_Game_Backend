package pl.lotto.numberreceiver;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Getter
public class Ticket {
    private String hash;
    private Set<Integer> numbersUser;
    private LocalDateTime drawDate;
}
