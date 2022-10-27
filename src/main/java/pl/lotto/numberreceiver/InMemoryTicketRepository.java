package pl.lotto.numberreceiver;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryTicketRepository implements TicketRepository {
    private final ConcurrentHashMap<String, Set<Integer>> map = new ConcurrentHashMap<>();

    @Override
    public Ticket save(Ticket ticket) {
        map.put(ticket.getHash(), ticket.getNumbers());
        return ticket;
    }

    public Set<Integer> findByHash(String hash){
        return map.get(hash);
    }
}
