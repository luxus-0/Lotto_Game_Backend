package pl.lotto.numberreceiver;

import pl.lotto.numberreceiver.dto.TicketDto;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryTicketRepository implements TicketRepository {
    private final ConcurrentHashMap<String, TicketDto> map = new ConcurrentHashMap<>();

    @Override
    public void save(Ticket ticket) {
        String hash = ticket.hash();
        TicketDto ticketDto = new TicketDto(ticket.numbers(), ticket.drawDate());
        map.put(hash, ticketDto);
    }

    @Override
    public Optional<TicketDto> findByHash(String hash) {
        TicketDto ticket = map.get(hash);
        return Optional.of(ticket);
    }

    @Override
    public Optional<TicketDto> findByDate(String dateDraw){
        TicketDto ticket = map.get(dateDraw);
        return Optional.of(ticket);
    }
}
