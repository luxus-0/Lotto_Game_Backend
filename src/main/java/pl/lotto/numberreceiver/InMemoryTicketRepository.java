package pl.lotto.numberreceiver;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryTicketRepository implements TicketRepository {
    private final ConcurrentHashMap<String, Ticket> map = new ConcurrentHashMap<>();

    @Override
    public void save(Ticket ticket) {
        String hash = ticket.getHash();
        Ticket ticketDto = new Ticket(ticket.getNumbers(), ticket.getDrawDate());
        map.put(hash, ticketDto);
    }

    @Override
    public Optional<Ticket> findByHash(String hash) {
        return Optional.of(map.get(hash));
    }
}
