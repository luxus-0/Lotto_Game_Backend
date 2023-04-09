package pl.lotto.domain.resultannouncer;

import pl.lotto.domain.resultannouncer.dto.ResultResponseDto;
import pl.lotto.domain.resultchecker.dto.ResultDto;

public class ResultLottoMapper {

    static ResultResponseDto mapToResultResponseDto(ResultLotto resultLotto, String message) {
        return ResultResponseDto.builder()
                .resultDto(ResultDto.builder()
                        .hash(resultLotto.hash())
                        .numbers(resultLotto.numbers())
                        .hitNumbers(resultLotto.hitNumbers())
                        .drawDate(resultLotto.drawDate())
                        .isWinner(resultLotto.isWinner())
                        .build())
                .message(message)
                .build();
    }

    static ResultDto mapToResultDtoSaved(ResultLotto resultLottoSaved) {
        return ResultDto.builder()
                .hash(resultLottoSaved.hash())
                .numbers(resultLottoSaved.numbers())
                .hitNumbers(resultLottoSaved.hitNumbers())
                .drawDate(resultLottoSaved.drawDate())
                .isWinner(resultLottoSaved.isWinner())
                .build();
    }

    static ResultLotto mapToResultLotto(ResultDto resultDto) {
        return ResultLotto.builder()
                .hash(resultDto.hash())
                .numbers(resultDto.numbers())
                .hitNumbers(resultDto.hitNumbers())
                .drawDate(resultDto.drawDate())
                .isWinner(resultDto.isWinner())
                .build();
    }
}
