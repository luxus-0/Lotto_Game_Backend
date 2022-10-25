package pl.lotto.numberreceiver;

import java.util.Set;

interface TicketRepository {
    Ticket save(Ticket ticket);

    Set<Integer> findByHash(String hash);

    Set<Integer> findByDate(String dateDraw);
}
