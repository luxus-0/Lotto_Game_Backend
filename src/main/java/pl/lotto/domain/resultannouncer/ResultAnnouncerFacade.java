package pl.lotto.domain.resultannouncer;

import lombok.AllArgsConstructor;
import pl.lotto.domain.resultannouncer.dto.ResultLottoResponseDto;
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

    ResultLottoResponseDto findResult(String hash) {
        if (resultAnnouncerRepository.existsById(hash)) {
            Optional<ResultLotto> resultByHash = resultAnnouncerRepository.findById(hash);
            if (resultByHash.isPresent()) {
                resultByHash.stream()
                        .map(resultLotto -> mapToResultLottoResponseDto(mapToResultDto(resultLotto)))
                        .findAny()
                        .ifPresent(resultLottoSaved -> resultAnnouncerRepository.save(mapToResultLotto(resultLottoSaved)));

            }
            ResultDto resultDto = resultsCheckerFacade.findByHash(hash);
            if (resultDto == null) {
                return ResultLottoResponseDto.builder()
                        .resultDto(null)
                        .message(HASH_NOT_EXIST)
                        .build();
            }
            if (resultAnnouncerRepository.existsById(hash) && !isAfterResultAnnouncementTime(resultDto)) {
                return new ResultLottoResponseDto(resultDto, WAIT_MESSAGE);
            }
            if (resultsCheckerFacade.findByHash(hash).isWinner()) {
                return new ResultLottoResponseDto(resultDto, WIN_MESSAGE);
            }
            return new ResultLottoResponseDto(resultDto, LOSE_MESSAGE);
        }
        throw new ResultLottoNotFoundException("Result lotto not found");
    }


    private boolean isAfterResultAnnouncementTime(ResultDto resultDto) {
        LocalDateTime announcementDateTime = resultDto.drawDate();
        return LocalDateTime.now(clock).isAfter(announcementDateTime);
    }
}
