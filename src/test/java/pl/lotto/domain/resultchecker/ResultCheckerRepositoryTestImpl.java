package pl.lotto.domain.resultchecker;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class ResultCheckerRepositoryTestImpl implements ResultCheckerRepository{
    private final Map<String, TicketResults> results = new ConcurrentHashMap<>();
    @Override
    public Optional<TicketResults> findAllByTicketUUID(String ticketUUID) {
        return Optional.ofNullable(results.get(ticketUUID));
    }

    @Override
    public <S extends TicketResults> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends TicketResults> List<S> insert(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends TicketResults> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends TicketResults> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends TicketResults> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends TicketResults> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends TicketResults> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends TicketResults> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends TicketResults, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public TicketResults save(TicketResults ticket) {
        return results.put(ticket.ticketUUID(), ticket);
    }

    @Override
    public <S extends TicketResults> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<TicketResults> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<TicketResults> findAll() {
        return null;
    }

    @Override
    public List<TicketResults> findAllById(Iterable<String> strings) {
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
    public void delete(TicketResults entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends TicketResults> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<TicketResults> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<TicketResults> findAll(Pageable pageable) {
        return null;
    }
}
