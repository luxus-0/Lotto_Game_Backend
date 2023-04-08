package pl.lotto.domain.resultannouncer;

import lombok.AllArgsConstructor;
import pl.lotto.domain.resultannouncer.dto.ResultResponseDto;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;
import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

import static pl.lotto.domain.resultannouncer.ResultLottoMapper.*;
import static pl.lotto.domain.resultannouncer.ResultStatus.*;

@AllArgsConstructor
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
        ResultLotto buildResultLotto = mapToResultLotto(resultDto);
        if (resultByHash.isPresent()) {
            return mapToResultResponseDto(buildResultLotto, ALREADY_CHECKED.message);
        }
        resultLottoRepository.save(buildResultLotto);
        ResultDto resultDtoSaved = mapToResultDtoSaved(buildResultLotto);
        if (!isAfterResultAnnouncementTime(resultDto)) {
            return new ResultResponseDto(resultDtoSaved, WAIT.message);
        } else if (resultDto.isWinner()) {
            return new ResultResponseDto(resultDtoSaved, WIN.message);
        } else {
            return new ResultResponseDto(resultDtoSaved, LOSE.message);
        }
    }

    private boolean isAfterResultAnnouncementTime(ResultDto resultDto) {
        LocalDateTime announcementDateTime = resultDto.drawDate();
        return LocalDateTime.now(clock).isAfter(announcementDateTime);
    }
}
