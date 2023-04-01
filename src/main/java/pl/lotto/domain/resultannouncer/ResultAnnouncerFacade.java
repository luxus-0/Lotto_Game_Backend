package pl.lotto.domain.resultannouncer;

import lombok.AllArgsConstructor;
import pl.lotto.domain.resultannouncer.dto.ResultResponseDto;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;
import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

import static pl.lotto.domain.resultannouncer.ResultAnnouncerMessage.*;
import static pl.lotto.domain.resultannouncer.ResultLottoMapper.*;

@AllArgsConstructor
public class ResultAnnouncerFacade {
    private final ResultsCheckerFacade resultsCheckerFacade;
    private final ResultAnnouncerRepository resultAnnouncerRepository;
    private final Clock clock;

    ResultResponseDto findResult(String hash) {
        ResultDto resultDto = resultsCheckerFacade.findByHash(hash);
        boolean isResultExistByHash = resultAnnouncerRepository.existsById(hash);
        if (isResultExistByHash) {
            Optional<ResultLotto> findResultByHash = resultAnnouncerRepository.findById(hash);

            if (findResultByHash.isPresent()) {
                findResultByHash.stream()
                        .map(resultLotto -> mapToResultLottoResponseDto(mapToResultDto(resultLotto)))
                        .findAny()
                        .ifPresent(resultLottoSaved -> resultAnnouncerRepository.save(mapToResultLotto(resultLottoSaved)));

            }
            if (resultDto == null) {
                return ResultResponseDto.builder()
                        .resultDto(null)
                        .message(HASH_NOT_EXIST)
                        .build();
            }
           if (!isAfterResultAnnouncementTime(resultDto)) {
                return new ResultResponseDto(resultDto, WAIT_MESSAGE);
            }
            if (resultDto.isWinner()) {
                return new ResultResponseDto(resultDto, WIN_MESSAGE);
            }
        }
        return new ResultResponseDto(resultDto, LOSE_MESSAGE);
    }

    private boolean isAfterResultAnnouncementTime(ResultDto resultDto) {
        LocalDateTime announcementDateTime = resultDto.drawDate();
        return LocalDateTime.now(clock).isAfter(announcementDateTime);
    }
}
