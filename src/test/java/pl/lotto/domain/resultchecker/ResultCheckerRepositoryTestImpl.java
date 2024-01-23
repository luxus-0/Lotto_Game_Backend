package pl.lotto.domain.resultchecker;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import pl.lotto.domain.winningnumbers.WinningTicket;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class ResultCheckerRepositoryTestImpl implements ResultCheckerRepository{
    private final Map<String, WinningTicket> results = new ConcurrentHashMap<>();
    @Override
    public Optional<WinningTicket> findAllByTicketUUID(String ticketUUID) {
        return Optional.ofNullable(results.get(ticketUUID));
    }

    @Override
    public <S extends WinningTicket> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends WinningTicket> List<S> insert(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends WinningTicket> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends WinningTicket> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends WinningTicket> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends WinningTicket> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends WinningTicket> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends WinningTicket> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends WinningTicket, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public WinningTicket save(WinningTicket winningTicket) {
        return results.put(winningTicket.ticketUUID(), winningTicket);
    }

    @Override
    public <S extends WinningTicket> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<WinningTicket> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<WinningTicket> findAll() {
        return null;
    }

    @Override
    public List<WinningTicket> findAllById(Iterable<String> strings) {
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
    public void delete(WinningTicket entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends WinningTicket> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<WinningTicket> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<WinningTicket> findAll(Pageable pageable) {
        return null;
    }
}
