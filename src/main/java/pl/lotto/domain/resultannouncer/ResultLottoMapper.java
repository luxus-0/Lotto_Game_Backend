package pl.lotto.domain.resultannouncer;

import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import pl.lotto.domain.resultchecker.dto.ResultDto;

public class ResultLottoMapper {

    static ResultDto mapToResultDtoSaved(ResultLotto resultLottoSaved) {
        return ResultDto.builder()
                .ticketId(resultLottoSaved.ticketId())
                .numbers(resultLottoSaved.numbers())
                .hitNumbers(resultLottoSaved.hitNumbers())
                .drawDate(resultLottoSaved.drawDate())
                .isWinner(resultLottoSaved.isWinner())
                .message(resultLottoSaved.message())
                .build();
    }

}
