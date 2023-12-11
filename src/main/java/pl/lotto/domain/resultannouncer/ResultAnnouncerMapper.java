package pl.lotto.domain.resultannouncer;

import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;

import static pl.lotto.domain.resultannouncer.ResultStatus.*;

public class ResultAnnouncerMapper {

    public static ResultAnnouncerResponseDto mapToResultAnnouncerResponseDto(ResultAnnouncerResponse resultAnnouncerResponseSaved) {
        return ResultAnnouncerResponseDto.builder()
                .ticketUUID(resultAnnouncerResponseSaved.ticketUUID())
                .inputNumbers(resultAnnouncerResponseSaved.numbers())
                .hitNumbers(resultAnnouncerResponseSaved.hitNumbers())
                .drawDate(resultAnnouncerResponseSaved.drawDate())
                .isWinner(resultAnnouncerResponseSaved.isWinner())
                .message(resultAnnouncerResponseSaved.message())
                .build();
    }

    public static ResultAnnouncerResponseDto toLoseResult(ResultAnnouncerResponseDto toResultAnnouncerResponseSavedDto) {
        return ResultAnnouncerResponseDto.builder()
                .ticketUUID(toResultAnnouncerResponseSavedDto.ticketUUID())
                .inputNumbers(toResultAnnouncerResponseSavedDto.inputNumbers())
                .hitNumbers(toResultAnnouncerResponseSavedDto.hitNumbers())
                .drawDate(toResultAnnouncerResponseSavedDto.drawDate())
                .isWinner(false)
                .message(LOSE.message)
                .build();
    }

    public static void toWinResult(ResultAnnouncerResponseDto toResultAnnouncerResponseSavedDto) {
        ResultAnnouncerResponseDto.builder()
                .ticketUUID(toResultAnnouncerResponseSavedDto.ticketUUID())
                .inputNumbers(toResultAnnouncerResponseSavedDto.inputNumbers())
                .hitNumbers(toResultAnnouncerResponseSavedDto.hitNumbers())
                .drawDate(toResultAnnouncerResponseSavedDto.drawDate())
                .isWinner(true)
                .message(WIN.message)
                .build();
    }

    public static ResultAnnouncerResponseDto toWaitMessageResult(ResultAnnouncerResponseDto toResultAnnouncerResponseSavedDto) {
        return ResultAnnouncerResponseDto.builder()
                .ticketUUID(toResultAnnouncerResponseSavedDto.ticketUUID())
                .inputNumbers(toResultAnnouncerResponseSavedDto.inputNumbers())
                .hitNumbers(toResultAnnouncerResponseSavedDto.hitNumbers())
                .drawDate(toResultAnnouncerResponseSavedDto.drawDate())
                .isWinner(toResultAnnouncerResponseSavedDto.isWinner())
                .message(WAIT.message)
                .build();
    }

}
