package pl.lotto.domain.numbersgenerator;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class WinningNumbersRepositoryTestImpl implements WinningNumbersRepository{
    private final Map<Set<Integer>, WinningNumbers> winningNumbersList = new ConcurrentHashMap<>();
    private final Map<LocalDateTime, WinningNumbers> winningNumbersByDate = new ConcurrentHashMap<>();

    @Override
    public Optional<WinningNumbers> findWinningNumbersByDrawDate(LocalDateTime drawDate) {
       return Optional.ofNullable(winningNumbersByDate.get(drawDate));
    }

    @Override
    public Optional<WinningNumbers> findWinningNumbersByHash(String hash) {
        return Optional.ofNullable(winningNumbersList.get(hash));
    }

    @Override
    public <S extends WinningNumbers> S save(S entity) {
        winningNumbersList.put(entity.winningNumbers(), entity);
        return entity;
    }

    @Override
    public WinningNumbers create(WinningNumbers winningNumbers) {
        winningNumbersByDate.put(winningNumbers.drawDate(), winningNumbers);
        return winningNumbers;
    }

    @Override
    public <S extends WinningNumbers> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<WinningNumbers> findById(String s) {
        return Optional.ofNullable(winningNumbersList.get(s));
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    public boolean existsByDrawDate(LocalDateTime drawDate) {
        winningNumbersList.get(drawDate);
        return true;
    }

    @Override
    public List<WinningNumbers> findAll() {
        return null;
    }

    @Override
    public Iterable<WinningNumbers> findAllById(Iterable<String> strings) {
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
    public void delete(WinningNumbers entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends WinningNumbers> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<WinningNumbers> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<WinningNumbers> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends WinningNumbers> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends WinningNumbers> List<S> insert(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends WinningNumbers> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends WinningNumbers> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends WinningNumbers> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends WinningNumbers> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends WinningNumbers> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends WinningNumbers> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends WinningNumbers, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}