package pl.lotto.domain.resultannouncer;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.lotto.domain.resultannouncer.dto.ResultResponseDto;
import pl.lotto.domain.resultchecker.ResultsCheckerFacade;
import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

import static pl.lotto.domain.resultannouncer.ResultAnnouncerMessage.*;
import static pl.lotto.domain.resultannouncer.ResultLottoMapper.*;

@AllArgsConstructor
@Log4j2
public class ResultAnnouncerFacade {
    public static final String RESULT_LOTTO_MESSAGE = "Result lotto not found";
    private final ResultsCheckerFacade resultsCheckerFacade;
    private final ResultLottoRepository resultLottoRepository;
    private final Clock clock;

    ResultResponseDto findResult(String hash) {
        ResultDto resultDto = resultsCheckerFacade.findByHash(hash);
        boolean isResultExistByHash = resultLottoRepository.existsById(hash);
        if (isResultExistByHash) {
            Optional<ResultLotto> findResultByHash = resultLottoRepository.findById(hash);

            if (findResultByHash.isPresent()) {
               ResultResponseDto resultResponseDto = findResultByHash.stream()
                        .map(ResultLottoMapper::mapToResultDto)
                        .map(ResultLottoMapper::mapToResultResponseDto)
                        .findAny()
                        .orElseThrow(() -> new ResultAnnouncerNotFoundException(RESULT_LOTTO_MESSAGE));

                        ResultLotto resultLotto = mapToResultLotto(resultResponseDto);
                        resultLottoRepository.save(resultLotto);
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
