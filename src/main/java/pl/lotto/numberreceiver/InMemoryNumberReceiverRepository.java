package pl.lotto.numberreceiver;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

class InMemoryNumberReceiverRepository implements NumberReceiverRepository {
    private final Map<UUID, UserNumbers> inMemoryUserNumbers = new ConcurrentHashMap<>();

    @Override
    public <S extends UserNumbers> S save(S entity) {
        inMemoryUserNumbers.put(entity.uuid(), entity);
        return entity;
    }

    @Override
    public UserNumbers findByDate(LocalDateTime dateTime) {
        return inMemoryUserNumbers.values()
                .stream()
                .filter(userNumbers -> userNumbers.dateTimeDraw().equals(dateTime))
                .findAny()
                .orElseThrow();
    }

    @Override
    public UserNumbers findByUUID(UUID uuid) {
        return inMemoryUserNumbers.values()
                .stream()
                .filter(userNumbers -> userNumbers.uuid().equals(uuid))
                .findAny()
                .orElseThrow();
    }

    @Override
    public void delete() {
        inMemoryUserNumbers.clear();
    }
    @Override
    public void deleteByUUID(UUID uuid) {
        inMemoryUserNumbers.remove(uuid);
    }

    public <S extends UserNumbers> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    public Optional<UserNumbers> findById(UUID uuid) {
        return Optional.empty();
    }

    public boolean existsById(UUID uuid) {
        return false;
    }

    public List<UserNumbers> findAll() {
        return null;
    }

    public Iterable<UserNumbers> findAllById(Iterable<UUID> uuids) {
        return null;
    }

    public long count() {
        return 0;
    }

    public void deleteById(UUID uuid) {

    }

    public void delete(UserNumbers entity) {

    }

    public void deleteAllById(Iterable<? extends UUID> uuids) {

    }

    public void deleteAll(Iterable<? extends UserNumbers> entities) {

    }

    public void deleteAll() {

    }

    public List<UserNumbers> findAll(Sort sort) {
        return null;
    }

    public Page<UserNumbers> findAll(Pageable pageable) {
        return null;
    }

    public <S extends UserNumbers> S insert(S s) {
        return null;
    }

    public <S extends UserNumbers> List<S> insert(Iterable<S> iterable) {
        return null;
    }

    public <S extends UserNumbers> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    public <S extends UserNumbers> List<S> findAll(Example<S> example) {
        return null;
    }

    public <S extends UserNumbers> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    public <S extends UserNumbers> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    public <S extends UserNumbers> long count(Example<S> example) {
        return 0;
    }

    public <S extends UserNumbers> boolean exists(Example<S> example) {
        return false;
    }

    public <S extends UserNumbers, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}

