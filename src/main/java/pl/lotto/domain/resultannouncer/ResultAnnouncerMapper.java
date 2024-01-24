package pl.lotto.domain.resultannouncer;

import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;

class ResultAnnouncerMapper {

    public static ResultAnnouncerResponseDto mapToResultLottoSaved(ResultAnnouncerResponse resultAnnouncerResponseSaved) {
        return ResultAnnouncerResponseDto.builder()
                .ticketUUID(resultAnnouncerResponseSaved.ticketUUID())
                .hitNumbers(resultAnnouncerResponseSaved.hitNumbers())
                .drawDate(resultAnnouncerResponseSaved.drawDate())
                .isWinner(resultAnnouncerResponseSaved.isWinner())
                .message(resultAnnouncerResponseSaved.message())
                .build();
    }

}
