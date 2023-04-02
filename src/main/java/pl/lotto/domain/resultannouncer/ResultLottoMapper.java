package pl.lotto.domain.resultannouncer;

import pl.lotto.domain.resultannouncer.dto.ResultResponseDto;
import pl.lotto.domain.resultchecker.dto.ResultDto;

public class ResultLottoMapper {

    static ResultResponseDto mapToResultResponseDto(ResultLotto resultLotto) {
        return ResultResponseDto.builder()
                .resultDto(ResultDto.builder()
                        .hash(resultLotto.hash())
                        .numbers(resultLotto.numbers())
                        .hitNumbers(resultLotto.hitNumbers())
                        .drawDate(resultLotto.drawDate())
                        .isWinner(resultLotto.isWinner())
                        .build())
                .build();
    }
}
