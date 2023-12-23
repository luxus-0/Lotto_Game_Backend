package pl.lotto.domain.resultannouncer;

import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;

public class ResultAnnouncerMapper {

    public static ResultAnnouncerResponseDto toResultLottoSaved(ResultAnnouncerResponse resultAnnouncerResponseSaved) {
        return ResultAnnouncerResponseDto.builder()
                .ticketUUID(resultAnnouncerResponseSaved.ticketUUID())
                .inputNumbers(resultAnnouncerResponseSaved.numbers())
                .hitNumbers(resultAnnouncerResponseSaved.hitNumbers())
                .drawDate(resultAnnouncerResponseSaved.drawDate())
                .isWinner(resultAnnouncerResponseSaved.isWinner())
                .message(resultAnnouncerResponseSaved.message())
                .build();
    }

}
