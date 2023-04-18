package pl.lotto.domain.resultannouncer;

import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import pl.lotto.domain.resultchecker.dto.ResultDto;

public class ResultLottoMapper {

    static ResultAnnouncerResponseDto mapToResultResponseDto(ResultLotto resultLotto, String message) {
        return ResultAnnouncerResponseDto.builder()
                .resultDto(ResultDto.builder()
                        .ticketId(resultLotto.ticketId())
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
                .ticketId(resultLottoSaved.ticketId())
                .numbers(resultLottoSaved.numbers())
                .hitNumbers(resultLottoSaved.hitNumbers())
                .drawDate(resultLottoSaved.drawDate())
                .isWinner(resultLottoSaved.isWinner())
                .build();
    }

    static ResultLotto mapToResultLotto(ResultDto resultDto) {
        return ResultLotto.builder()
                .ticketId(resultDto.ticketId())
                .numbers(resultDto.numbers())
                .hitNumbers(resultDto.hitNumbers())
                .drawDate(resultDto.drawDate())
                .isWinner(resultDto.isWinner())
                .build();
    }
}
