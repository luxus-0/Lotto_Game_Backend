package pl.lotto.numberreceiver;

import java.util.Optional;

interface TicketRepository {
    void save(Ticket ticket);
    Optional<Ticket> findByHash(String hash);
    Optional<Ticket> findByDate(String dateDraw);
}
