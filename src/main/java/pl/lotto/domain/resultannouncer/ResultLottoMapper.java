package pl.lotto.domain.resultannouncer;

import pl.lotto.domain.resultannouncer.dto.ResultResponseDto;
import pl.lotto.domain.resultchecker.dto.ResultDto;

public class ResultLottoMapper {

    static ResultResponseDto mapToResultResponseDto(ResultDto resultLottoDto) {
        return ResultResponseDto.builder()
                .resultDto(resultLottoDto)
                .build();
    }

    static ResultLotto mapToResultLotto(ResultResponseDto resultLottoSaved) {
        return ResultLotto.builder()
                .hash(resultLottoSaved.resultDto().hash())
                .numbers(resultLottoSaved.resultDto().numbers())
                .hitNumbers(resultLottoSaved.resultDto().hitNumbers())
                .drawDate(resultLottoSaved.resultDto().drawDate())
                .isWinner(resultLottoSaved.resultDto().isWinner())
                .build();
    }

   static ResultDto mapToResultDto(ResultLotto resultLotto) {
        return ResultDto.builder()
                .hash(resultLotto.hash())
                .numbers(resultLotto.numbers())
                .hitNumbers(resultLotto.hitNumbers())
                .drawDate(resultLotto.drawDate())
                .isWinner(resultLotto.isWinner())
                .build();
    }
}
