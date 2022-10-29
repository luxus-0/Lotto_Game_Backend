package pl.lotto.numberreceiver;

import java.time.LocalDateTime;
import java.util.Set;

class TicketGenerator {
    private final TicketRandomUUID ticketUUID;
    private final TicketDrawDate ticketDrawDate;

    TicketGenerator(TicketRandomUUID ticketUUID, TicketDrawDate ticketDrawDate) {
        this.ticketUUID = ticketUUID;
        this.ticketDrawDate = ticketDrawDate;
    }

    Ticket generateTicket(Set<Integer> inputNumbers, LocalDateTime drawDateTime) {
        String uuid = ticketUUID.generateUUID();
        LocalDateTime drawDate = ticketDrawDate.generateDrawDate(drawDateTime);
        return generate(uuid, inputNumbers, drawDate);
    }

    private Ticket generate(String hash, Set<Integer> numbersUser, LocalDateTime drawDate) {
        return Ticket.builder()
                .hash(hash)
                .numbersUser(numbersUser)
                .drawDate(drawDate)
                .build();
    }
}
