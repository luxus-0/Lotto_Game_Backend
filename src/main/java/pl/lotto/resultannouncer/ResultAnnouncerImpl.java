package pl.lotto.resultannouncer;

import org.springframework.stereotype.Service;
import pl.lotto.resultannouncer.dto.ResultMessageDto;

import java.util.Set;
import java.util.stream.Collectors;

@Service
class ResultAnnouncerImpl {
    private final ResultAnnouncerRepository resultAnnouncerRepository;

    ResultAnnouncerImpl(ResultAnnouncerRepository resultAnnouncerRepository) {
        this.resultAnnouncerRepository = resultAnnouncerRepository;
    }

    Set<ResultMessageDto> readResultForUser(String message){
        return resultAnnouncerRepository.findByResult(message)
                .stream()
                .map(resultMessage -> new ResultMessageDto(message))
                .collect(Collectors.toSet());
    }
}
