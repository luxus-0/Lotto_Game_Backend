package pl.lotto.numberreceiver;

import java.util.Set;

interface TicketRepository {
    Ticket save(Ticket ticket);
    Set<Integer> findAll(String uuid);
}
