package pl.lotto.domain.numberreceiver;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class InMemoryTicketRepository implements TicketRepository {

    private final Map<String, Ticket> tickets = new ConcurrentHashMap<>();

    @Override
    public List<Ticket> findAllByDrawDate(LocalDateTime drawDate) {
        return tickets.values()
                .stream()
                .filter(ticket -> ticket.drawDate().isEqual(drawDate))
                .collect(Collectors.toList());
    }

    @Override
    public <S extends Ticket> S save(S entity) {
        tickets.put(entity.hash(), entity);
        return entity;
    }

    @Override
    public <S extends Ticket> List<S> saveAll(Iterable<S> entities) {
        Stream<S> stream = StreamSupport.stream(entities.spliterator(), false);
        List<S> list = stream.toList();
        list.forEach(ticket -> tickets.put(ticket.hash(), ticket));
        return list;
    }

    @Override
    public Optional<Ticket> findById(String id) {
        return Optional.ofNullable(tickets.get(id));
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<Ticket> findAll() {
        return null;
    }

    @Override
    public Iterable<Ticket> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(Ticket entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Ticket> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Ticket> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Ticket> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Ticket> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends Ticket> List<S> insert(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends Ticket> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Ticket> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Ticket> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Ticket> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Ticket> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Ticket> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Ticket, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
