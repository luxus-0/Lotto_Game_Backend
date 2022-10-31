package pl.lotto.numberreceiver;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class InMemoryTicketRepository implements TicketRepository {
    private final Map<String, Set<Integer>> map = new HashMap<>();

    @Override
    public Ticket save(Ticket ticket) {
        map.put(ticket.hash(), ticket.numbersUser());
        return ticket;
    }

    @Override
    public Set<Integer> findAll(String uuid){
        return map.get(uuid);
    }
}
