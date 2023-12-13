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

    public static ResultAnnouncerResponseDto getResultTicket(ResultAnnouncerResponseDto toResultAnnouncerResponseSavedDto, boolean isWinner, String message) {
        return ResultAnnouncerResponseDto.builder()
                .ticketUUID(toResultAnnouncerResponseSavedDto.ticketUUID())
                .inputNumbers(toResultAnnouncerResponseSavedDto.inputNumbers())
                .hitNumbers(toResultAnnouncerResponseSavedDto.hitNumbers())
                .drawDate(toResultAnnouncerResponseSavedDto.drawDate())
                .isWinner(isWinner)
                .message(message)
                .build();
    }

    public static ResultAnnouncerResponseDto getResultTicket(ResultAnnouncerResponseDto toResultAnnouncerResponseSavedDto, String message) {
        return ResultAnnouncerResponseDto.builder()
                .ticketUUID(toResultAnnouncerResponseSavedDto.ticketUUID())
                .inputNumbers(toResultAnnouncerResponseSavedDto.inputNumbers())
                .hitNumbers(toResultAnnouncerResponseSavedDto.hitNumbers())
                .drawDate(toResultAnnouncerResponseSavedDto.drawDate())
                .message(message)
                .build();
    }

}
