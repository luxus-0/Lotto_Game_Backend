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

public class ResultAnnouncerRepositoryTestImpl implements ResultAnnouncerRepository{
    private final Map<String, ResultAnnouncerResponse> responseList = new ConcurrentHashMap<>();

    @Override
    public Optional<ResultAnnouncerResponse> findAllByTicketUUID(String ticketUUID) {
        return Optional.ofNullable(responseList.get(ticketUUID));
    }

    @Override
    public <S extends ResultAnnouncerResponse> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends ResultAnnouncerResponse> List<S> insert(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends ResultAnnouncerResponse> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends ResultAnnouncerResponse> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends ResultAnnouncerResponse> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends ResultAnnouncerResponse> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends ResultAnnouncerResponse> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends ResultAnnouncerResponse> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends ResultAnnouncerResponse, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public ResultAnnouncerResponse save(ResultAnnouncerResponse resultResponse) {
        return responseList.put(resultResponse.ticketUUID(), resultResponse);
    }

    @Override
    public <S extends ResultAnnouncerResponse> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<ResultAnnouncerResponse> findById(String ticketId) {
        return Optional.ofNullable(responseList.get(ticketId));
    }

    @Override
    public boolean existsById(String ticketId) {
        return responseList.containsKey(ticketId);
    }

    @Override
    public List<ResultAnnouncerResponse> findAll() {
        return null;
    }

    @Override
    public List<ResultAnnouncerResponse> findAllById(Iterable<String> strings) {
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
    public void delete(ResultAnnouncerResponse entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends ResultAnnouncerResponse> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<ResultAnnouncerResponse> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<ResultAnnouncerResponse> findAll(Pageable pageable) {
        return null;
    }
}
