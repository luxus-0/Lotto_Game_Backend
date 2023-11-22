package pl.lotto.domain.resultannouncer;

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

public class InMemoryResultLottoRepository implements ResultLottoRepository {
    private final Map<String, ResultLotto> responseList = new ConcurrentHashMap<>();

    @Override
    public ResultLotto save(ResultLotto resultLotto) {
        return responseList.put(resultLotto.ticketId(), resultLotto);
    }

    @Override
    public Optional<ResultLotto> findByTicketId(String ticketId) {
        return Optional.ofNullable(responseList.get(ticketId));
    }

    @Override
    public boolean existsById(String hash) {
        return responseList.containsKey(hash);
    }


    @Override
    public <S extends ResultLotto> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<ResultLotto> findById(String s) {
        return Optional.empty();
    }

    @Override
    public List<ResultLotto> findAll() {
        return null;
    }

    @Override
    public List<ResultLotto> findAllById(Iterable<String> strings) {
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
    public void delete(ResultLotto entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends ResultLotto> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<ResultLotto> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<ResultLotto> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends ResultLotto> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends ResultLotto> List<S> insert(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends ResultLotto> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends ResultLotto> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends ResultLotto> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends ResultLotto> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends ResultLotto> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends ResultLotto> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends ResultLotto, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
