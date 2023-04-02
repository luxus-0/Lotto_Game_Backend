package pl.lotto.domain.resultannouncer;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.lotto.domain.resultannouncer.dto.ResultResponseDto;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;
import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

import static pl.lotto.domain.resultannouncer.ResultLottoMapper.mapToResultResponseDto;
import static pl.lotto.domain.resultannouncer.ResultStatus.*;

@AllArgsConstructor
@Log4j2
public class ResultAnnouncerFacade {
    private final ResultsCheckerFacade resultsCheckerFacade;
    private final ResultLottoRepository resultLottoRepository;
    private final Clock clock;

    ResultResponseDto findResult(String hash) {
        ResultDto resultDto = resultsCheckerFacade.findByHash(hash);

        if (resultDto == null) {
            return ResultResponseDto.builder()
                    .resultDto(null)
                    .message(HASH_NOT_EXIST.message)
                    .build();
        }
        Optional<ResultLotto> resultByHash = resultLottoRepository.findById(hash);
        if (resultByHash.isPresent()) {
           ResultLotto resultLotto = getResultResponse(resultByHash.get());
           ResultLotto resultLottoSaved = resultLottoRepository.save(resultLotto);
           return mapToResultResponseDto(resultLottoSaved);
        }

        if (!isAfterResultAnnouncementTime(resultDto)) {
            return new ResultResponseDto(resultDto, WAIT.message);
        } else if (resultDto.isWinner()) {
            return new ResultResponseDto(resultDto, WIN.message);
        } else {
            return new ResultResponseDto(resultDto, LOSE.message);
        }
    }

    private ResultLotto getResultResponse(ResultLotto resultLotto) {
        return Optional.ofNullable(resultLotto).stream()
                .findAny()
                .orElseThrow(() -> new ResultLottoNotFoundException(RESULT_MESSAGE_EXCEPTION.message));
    }

    private boolean isAfterResultAnnouncementTime(ResultDto resultDto) {
        LocalDateTime announcementDateTime = resultDto.drawDate();
        return LocalDateTime.now(clock).isAfter(announcementDateTime);
    }
}
