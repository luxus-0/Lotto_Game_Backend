package pl.lotto.numberreceiver;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

class TicketGenerator {
    String generateHash() {
        return UUID.randomUUID().toString();
    }

    LocalDateTime generateDate(){
        return LocalDateTime.now();
    }

    public Ticket generateTicket(Set<Integer> inputNumbers, String hash, LocalDateTime date) {
        return Ticket.builder()
                .hash(hash)
                .numbers(new TreeSet<>(inputNumbers))
                .drawDate(date)
                .build();
    }
}
